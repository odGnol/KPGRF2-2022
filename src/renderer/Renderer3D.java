package renderer;

import model3d.Scene;
import model3d.Solid;
import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import rasterize.Raster;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.List;
import java.util.Optional;

public class Renderer3D implements GPURenderer {

    private final LineRasterizer lineRasterizer;
    private final Raster raster;

    private Mat4 model, view, projection;

    public Renderer3D(Raster raster) {
        this.raster = raster;
        this.lineRasterizer = new LineRasterizerGraphics(raster);

        model = new Mat4Identity();
        view = new Mat4Identity();
        projection = new Mat4Identity();
    }

    @Override
    public void draw(Scene scene) {
        List<Solid> solids = scene.getSolids();
        for (Solid solid : solids) {
            List<Point3D> vb = solid.getVertexBuffer();
            List<Integer> ib = solid.getIndexBuffer();
            for (int i = 0; i < ib.size(); i += 2) {
                Integer i1 = ib.get(i);
                Integer i2 = ib.get(i + 1);
                Point3D p1 = vb.get(i1);
                Point3D p2 = vb.get(i2);
                transformLine(p1, p2, solid.getColor());
            }
        }
    }

    private void transformLine(Point3D a, Point3D b, int color) {
        a = a.mul(model).mul(view).mul(projection);
        b = b.mul(model).mul(view).mul(projection);

        if (clip(a)) return;
        if (clip(b)) return;

        Optional<Vec3D> dehomogA = a.dehomog();
        Optional<Vec3D> dehomogB = b.dehomog();

        if (dehomogA.isEmpty() || dehomogB.isEmpty()) return;
//        if (!dehomogA.isPresent() || !dehomogB.isPresent()) return;

        Vec3D v1 = dehomogA.get();
        Vec3D v2 = dehomogB.get();

        Vec3D vv1 = transformToWindow(v1);
        Vec3D vv2 = transformToWindow(v2);

        lineRasterizer.rasterize(
                (int) Math.round(vv1.getX()),
                (int) Math.round(vv1.getY()),
                (int) Math.round(vv2.getX()),
                (int) Math.round(vv2.getY()),
                color
        );
    }

    private boolean clip(Point3D p) {
        // přednáška PGI_F, slide 78
        return false; // TODO
    }

    private Vec3D transformToWindow(Vec3D vec) {
        // přednáška PGI_F, slide 90
        return vec
                .mul(new Vec3D(1, -1, 1)) // Y jde nahoru a my chceme, aby šlo dolů
                .add(new Vec3D(1, 1, 0)) // (0, 0) je uprostřed a my chceme, aby bylo vlevo nahoře
                .mul(new Vec3D(raster.getWidth() / 2.0, raster.getHeight() / 2.0, 1));
                // máme <0;2> -> vynásobíme polovinou velikosti plátna
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
