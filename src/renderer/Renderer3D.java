package renderer;

import model.Cast;
import model.TypGeometrickeTopologie;
import model.Vrchol;
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
    public void nakresli(List<Cast> casti, List<Integer> ib, List<Vrchol> vb) {
        for (Cast cast : casti) {
            TypGeometrickeTopologie topologyType = cast.getGeometrickyTyp();
            int index = cast.getIndex();
            int pocet = cast.getPocet();
            if (topologyType == TypGeometrickeTopologie.TROJUHELNIK) {
                for (int i = index; i < pocet * 3 + index; i += 3) {
                    Integer i1 = ib.get(i);
                    Integer i2 = ib.get(i + 1);
                    Integer i3 = ib.get(i + 2);

                    Vrchol v1 = vb.get(i1);
                    Vrchol v2 = vb.get(i2);
                    Vrchol v3 = vb.get(i3);
                    pripravTrojuhelnik(v1, v2, v3);
                }
            } else if (topologyType == TypGeometrickeTopologie.USECKA) {
                // TODO
            } // ...
        }
    }

    private void pripravTrojuhelnik(Vrchol v1, Vrchol v2, Vrchol v3) {
        // 1. transformace vrcholů
        Vrchol a = new Vrchol(
                v1.getBod().mul(model).mul(view).mul(projection),
                v1.getBarva()
        );
        Vrchol b = new Vrchol(
                v2.getBod().mul(model).mul(view).mul(projection),
                v2.getBarva()
        );
        Vrchol c = new Vrchol(
                v3.getBod().mul(model).mul(view).mul(projection),
                v3.getBarva()
        );

        // 2. ořezání
        // ořezat trojúhelníky, které jsou CELÉ mimo zobrazovací objem
        // slides 86 a 97

        // podle podle osy x
        if (a.getX() > a.getW() && b.getX() > b.getW() && c.getX() > c.getW()) return; // trojúhelník je moc vpravo
        if (a.getX() < -a.getW() && b.getX() < -b.getW() && c.getX() < -c.getW()) return; // moc vlevo

        // podle osy y
        if (a.getY() > a.getW() && b.getY() > b.getW() && c.getY() > c.getW()) return; // moc nahoře
        if (a.getY() < -a.getW() && b.getY() < -b.getW() && c.getY() < -c.getW()) return; // moc dole

        // podle osy z
        if (a.getZ() > a.getW() && b.getZ() > b.getW() && c.getZ() > c.getW()) return; // moc vzadu/daleko (?)
        if (a.getZ() < 0 && b.getZ() < 0 && c.getZ() < 0) return; // je za námi

        // 3. seřazení podle Z
        // slide 101
        // od javy 11 je možné využít var proměnnou, jen pro lokální proměnnou: var docasne = a;
        if (a.getZ() < b.getZ()) {
            Vrchol docasne = a;
            a = b;
            b = docasne;
        }
        if (b.getZ() < c.getZ()) {
            Vrchol docasne = b;
            b = c;
            c = docasne;
        }
        // teď je v C vrchol, který je nám nejblíže
        if (a.getZ() < b.getZ()) {
            Vrchol docasne = a;
            a = b;
            b = docasne;
        }
//        List<Vertex> collect = Stream.of(a, b, c).sorted(Comparator.comparingDouble(Vertex::getZ)).toList(); // ? not tested

        // teď máme seřazeno - Z je od největšího po nejmenší: A, B, C

        // 4. ořezání podle hrany Z
        // slide 101
        // lineární interpolace
        if (a.getZ() < 0) {
            // A.Z je menší než nula = > všechny Z jsou menší než nula => není co zobrazit
        } else if (b.getZ() < 0) {
            // vrchol A je vidět, vrcholy B a C nejsou
            // odečíst minimum: 0 - a.getZ() | dělit rozsahem: b.getZ() - a.getZ()
            double t1 = (0 - a.getZ()) / (b.getZ() - a.getZ());
            // 0 -> protože ten nový vrchol (ab), který má vzniknout, bude mít souřadnici Z nula
            Vrchol ab = a.mul(1 - t1).add(b.mul(t1));

            double t2 = -a.getZ() / (c.getZ() - a.getZ());
            Vrchol ac = a.mul(1 - t2).add(b.mul(t2));

            nakresliTrojuhelnik(a, ab, ac);

        } else if (c.getZ() < 0) {
            // 2 krát se zavolá funkce nakresliTrojuhelnik()

            double t1 = - a.getZ() / c.getZ() - a.getZ();
            Vrchol ac = a.mul(1 - t1).add(c.mul(t1));

            double t2 = -b.getZ() / c.getZ() - b.getZ();
            Vrchol bc = b.mul(1 - t2).add(c.mul(t2));

            nakresliTrojuhelnik(a, b, bc);
            nakresliTrojuhelnik(a, ac, bc);
        } else {
            // vidíme celý trojúhelník
            nakresliTrojuhelnik(a, b, c);
        }
    }

    private void nakresliTrojuhelnik(Vrchol a, Vrchol b, Vrchol c) {
        // 1. dehomogenizace
        Optional<Vrchol> dA = a.dehomog();
        Optional<Vrchol> dB = b.dehomog();
        Optional<Vrchol> dC = c.dehomog();

        // zahodit trojúhelník, pokud některý vrchol má w==0 (nelze provést dehomogenizaci)
        if (dA.isEmpty() || dB.isEmpty() || dC.isEmpty()) return;

        Vrchol v1 = dA.get();
        Vrchol v2 = dB.get();
        Vrchol v3 = dC.get();

        // 2. transformace do okna
        Vec3D vec3D1 = transformujDoOkna(v1);
        Vrchol aa = new Vrchol(new Point3D(vec3D1), v1.getBarva());

        Vec3D vec3D2 = transformujDoOkna(v2);
        Vrchol bb = new Vrchol(new Point3D(vec3D2), v2.getBarva());

        Vec3D vec3D3 = transformujDoOkna(v3);
        Vrchol cc = new Vrchol(new Point3D(vec3D3), v3.getBarva());

        // 3. seřazení podle Y
        if (aa.getY() > bb.getY()) {
            Vrchol temp = aa;
            aa = bb;
            bb = temp;
        }
        if (bb.getY() > cc.getY()) {
            Vrchol temp = bb;
            bb = cc;
            cc = temp;
        }
        if (aa.getY() > bb.getY()) {
            Vrchol temp = aa;
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
            Vrchol d = aa.mul(1 - t1).add(bb.mul(t1));

            double t2 = (y - aa.getY()) / (cc.getY() - aa.getY());
            Vrchol e = aa.mul(1 - t2).add(cc.mul(t2));

            naplnUsecku(d, e);
        }
        // 2. for cyklus B->C
        // TODO
    }

    private void naplnUsecku(Vrchol a, Vrchol b) {
        if (a.getX() > b.getX()) {
            Vrchol temp = a;
            a = b;
            b = temp;
        }

        int xStart = Math.max(0, (int) a.getX() + 1);
        double xEnd = Math.min(imageBuffer.getWidth() - 1, b.getX());

        for (int x = xStart; x <= xEnd; x++) {
            double t = (x - a.getX()) / (b.getX() - a.getX());
            Vrchol ab = a.mul(1 - t).add(b.mul(t));

            nakresliPixel(x, (int) Math.round(ab.getY()), ab.getZ(), ab.getBarva());
        }
    }

    private void nakresliPixel(int x, int y, double z, Col color) {
        Optional<Double> zOptional = depthBuffer.getElement(x, y);
        if (zOptional.isPresent() && zOptional.get() > z) {
            depthBuffer.setElement(x, y, z);
            imageBuffer.setElement(x, y, color.getRGB());
        }
    }

    private Vec3D transformujDoOkna(Vrchol vertex) {
        // přednáška PGI_F, slide 90
        return new Vec3D(vertex.getBod())
                .mul(new Vec3D(1, -1, 1)) // Y jde nahoru a my chceme, aby šlo dolů
                .add(new Vec3D(1, 1, 0)) // (0, 0) je uprostřed a my chceme, aby bylo vlevo nahoře
                .mul(new Vec3D(imageBuffer.getWidth() / 2.0, imageBuffer.getHeight() / 2.0, 1));
        // máme <0;2> -> vynásobíme polovinou velikosti plátna
    }

    @Override
    public void procisti() {
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
