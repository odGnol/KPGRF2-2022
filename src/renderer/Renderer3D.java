package renderer;

import model.Part;
import model.TopologyType;
import model.Vertex;
import rasterize.DepthBuffer;
import rasterize.Raster;
import transforms.*;

import java.util.List;
import java.util.Optional;

public class Renderer3D implements GPURenderer {

    private final Raster<Integer> imageBuffer;
    private final DepthBuffer depthBuffer;

    private Mat4 model, view, projection;

    public Renderer3D(Raster<Integer> raster) {
        this.imageBuffer = raster;
        depthBuffer = new DepthBuffer(raster.getWidth(), raster.getHeight());

        model = new Mat4Identity();
        view = new Mat4Identity();
        projection = new Mat4Identity();
    }

    @Override
    public void draw(List<Part> parts, List<Integer> ib, List<Vertex> vb) {
        for (Part part : parts) {
            TopologyType topologyType = part.getTopologyType();
            int index = part.getIndex();
            int count = part.getCount();
            if (topologyType == TopologyType.TRIANGLE) {
                for (int i = index; i < count * 3 + index; i += 3) {
                    Integer i1 = ib.get(i);
                    Integer i2 = ib.get(i + 1);
                    Integer i3 = ib.get(i + 2);

                    Vertex v1 = vb.get(i1);
                    Vertex v2 = vb.get(i2);
                    Vertex v3 = vb.get(i3);
                    prepareTriangle(v1, v2, v3);
                }
            } else if (topologyType == TopologyType.LINE) {
                // TODO
            } // ...
        }
    }

    private void prepareTriangle(Vertex v1, Vertex v2, Vertex v3) {
        // 1. transformace vrcholů
        Vertex a = new Vertex(
                v1.getPoint().mul(model).mul(view).mul(projection),
                v1.getColor()
        );
        Vertex b = new Vertex(
                v2.getPoint().mul(model).mul(view).mul(projection),
                v2.getColor()
        );
        Vertex c = new Vertex(
                v3.getPoint().mul(model).mul(view).mul(projection),
                v3.getColor()
        );

        // 2. ořezání
        // ořezat trojúhleníky, které jsou CELÉ mimo zobrazovací objem
        // slides 86 a 97
        if (a.getX() > a.getW() && b.getX() > b.getW() && c.getX() > c.getW()) return; // trojúhelník je moc vpravo
        if (a.getX() < -a.getW() && b.getX() < -b.getW() && c.getX() < -c.getW()) return; // moc vlevo
        // TODO podle y
        if (a.getZ() > a.getW() && b.getZ() > b.getW() && c.getZ() > c.getW()) return; // moc vzadu/daleko (?)
        if (a.getZ() < 0 && b.getZ() < 0 && c.getZ() < 0) return; // je za námi

        // 3. seřazení podle Z
        // slide 101
        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        if (b.getZ() < c.getZ()) {
//            var temp = b;
            Vertex temp = b;
            b = c;
            c = temp;
        }
        // teď je v C vrchol, který je nám nejblíže
        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
//        List<Vertex> collect = Stream.of(a, b, c).sorted(Comparator.comparingDouble(Vertex::getZ)).toList(); // ? not tested

        // teď máme seřazeno - Z je od největšího po nejmenší: A, B, C

        // 4. ořezání podle hrany Z
        // slide 101
        if (a.getZ() < 0) {
            // A.Z je menší než nula = > všechny Z jsou menší než nula => není co zobrazit
        } else if (b.getZ() < 0) {
            // vrchol A je vidět, vrcholy B a C nejsou
            double t1 = (0 - a.getZ()) / (b.getZ() - a.getZ());
            // 0 -> protože ten nový vrchol (ab), který má vzniknout, bude mít souřadnici Z nula
            Vertex ab = a.mul(1 - t1).add(b.mul(t1));


            double t2 = -a.getZ() / (c.getZ() - a.getZ());
            Vertex ac = a.mul(1 - t2).add(b.mul(t2));

            drawTriangle(a, ab, ac);

        } else if (c.getZ() < 0) {
            // TODO
            // 2 krát se zavolá funkce drawTriangle()
        } else {
            // vidíme celý trojúhelník
            drawTriangle(a, b, c);
        }
    }

    private void drawTriangle(Vertex a, Vertex b, Vertex c) {
        // 1. dehomogenizace
        Optional<Vertex> dA = a.dehomog();
        Optional<Vertex> dB = b.dehomog();
        Optional<Vertex> dC = c.dehomog();

        // zahodit trojúhelník, pokud některý vrchol má w==0 (nelze provést dehomogenizaci)
        if (dA.isEmpty() || dB.isEmpty() || dC.isEmpty()) return;

        Vertex v1 = dA.get();
        Vertex v2 = dB.get();
        Vertex v3 = dC.get();

        // 2. transformace do okna
        Vec3D vec3D1 = transformToWindow(v1);
        Vertex aa = new Vertex(new Point3D(vec3D1), v1.getColor());

        Vec3D vec3D2 = transformToWindow(v2);
        Vertex bb = new Vertex(new Point3D(vec3D2), v2.getColor());

        Vec3D vec3D3 = transformToWindow(v3);
        Vertex cc = new Vertex(new Point3D(vec3D3), v3.getColor());

        // 3. seřazení podle Y
        if (aa.getY() > bb.getY()) {
            Vertex temp = aa;
            aa = bb;
            bb = temp;
        }
        if (bb.getY() > cc.getY()) {
            Vertex temp = bb;
            bb = cc;
            cc = temp;
        }
        if (aa.getY() > bb.getY()) {
            Vertex temp = aa;
            aa = bb;
            bb = temp;
        }

        // 4. interpolace podle Y
        // slides 129, 130
        // 1. for cyklus A->B
        int yStart = Math.max(0, (int) aa.getY() + 1);
        double yEnd = Math.min(imageBuffer.getHeight() - 1, bb.getY());

        for (int y = yStart; y <= yEnd; y++) {
            double t1 = (y - aa.getY()) / (bb.getY() - aa.getY());
            Vertex d = aa.mul(1 - t1).add(bb.mul(t1));

            double t2 = (y - aa.getY()) / (cc.getY() - aa.getY());
            Vertex e = aa.mul(1 - t2).add(cc.mul(t2));

            fillLine(d, e);
        }
        // 2. for cyklus B->C
        // TODO
    }

    private void fillLine(Vertex a, Vertex b) {
        if (a.getX() > b.getX()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        int xStart = Math.max(0, (int) a.getX() + 1);
        double xEnd = Math.min(imageBuffer.getWidth() - 1, b.getX());

        for (int x = xStart; x <= xEnd; x++) {
            double t = (x - a.getX()) / (b.getX() - a.getX());
            Vertex ab = a.mul(1 - t).add(b.mul(t));

            drawPixel(x, (int) Math.round(ab.getY()), ab.getZ(), ab.getColor());
        }
    }

    private void drawPixel(int x, int y, double z, Col color) {
        Optional<Double> zOptional = depthBuffer.getElement(x, y);
        if (zOptional.isPresent() && zOptional.get() > z) {
            depthBuffer.setElement(x, y, z);
            imageBuffer.setElement(x, y, color.getRGB());
        }
    }

    private Vec3D transformToWindow(Vertex vertex) {
        // přednáška PGI_F, slide 90
        return new Vec3D(vertex.getPoint())
                .mul(new Vec3D(1, -1, 1)) // Y jde nahoru a my chceme, aby šlo dolů
                .add(new Vec3D(1, 1, 0)) // (0, 0) je uprostřed a my chceme, aby bylo vlevo nahoře
                .mul(new Vec3D(imageBuffer.getWidth() / 2.0, imageBuffer.getHeight() / 2.0, 1));
                // máme <0;2> -> vynásobíme polovinou velikosti plátna
    }

    @Override
    public void clear() {
        imageBuffer.clear();
        depthBuffer.clear();
    }

    @Override
    public void setModel(Mat4 model) {
        this.model = model;
    }

    @Override
    public void setView(Mat4 view) {
        this.view = view;
    }

    @Override
    public void setProjection(Mat4 projection) {
        this.projection = projection;
    }

}
