package control;

import model.Cast;
import model.TypGeometrickeTopologie;
import model.Vrchol;
import rasterize.Raster;
import renderer.GPURenderer;
import renderer.Renderer3D;
import transforms.*;
import view.Panel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Controller3D {

    private final Panel panel;
    private final GPURenderer renderer;
    private final Raster<Integer> obrazBuffer;

    private Mat4 model, projection;
    private Camera camera;

    private List<Vrchol> vrcholBuffer;
    private List<Integer> indexBuffer;
    private List<Cast> castBuffer;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.obrazBuffer = panel.getImageBuffer();
        this.renderer = new Renderer3D(panel.getImageBuffer());

        initBuffers();
        initMatrices();
        initListeners(panel);

        // test draw
//        imageBuffer.setElement(50, 50, Color.YELLOW.getRGB());
//        panel.repaint();

        zobraz();
    }

    private void initBuffers() {
        vrcholBuffer = new ArrayList<>();
        vrcholBuffer.add(new Vrchol(new Point3D(-1, -1, 1), new Col(1.0, 1.0, 0.0)));
        vrcholBuffer.add(new Vrchol(new Point3D(1, -1, 1), new Col(255, 255, 0)));
        vrcholBuffer.add(new Vrchol(new Point3D(1, 1, 1), new Col(Color.BLUE.getRGB())));
        vrcholBuffer.add(new Vrchol(new Point3D(-1, 1, 1), new Col(Color.GREEN.getRGB())));

        vrcholBuffer.add(new Vrchol(new Point3D(-1, -1, -1), new Col(Color.ORANGE.getRGB())));
        vrcholBuffer.add(new Vrchol(new Point3D(1, -1, -1), new Col(Color.CYAN.getRGB())));
        vrcholBuffer.add(new Vrchol(new Point3D(1, 1, -1), new Col(Color.RED.getRGB())));
        vrcholBuffer.add(new Vrchol(new Point3D(-1, 1, -1), new Col(Color.WHITE.getRGB())));

        indexBuffer = new ArrayList<>();
        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(2);

        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(3);

//        indexBuffer.addAll(Arrays.asList(0, 1, 2));
//        indexBuffer.addAll(Arrays.asList(0, 1, 3));

        // TODO chybí dalších 5 stěn krychle pro index buffer

        castBuffer = new ArrayList<>();
        castBuffer.add(new Cast(TypGeometrickeTopologie.TROJUHELNIK, 0, 2)); // FIXME count bude 12 až bude celý IB
        // teď by byl error, protože je málo indexů
    }

    private void initMatrices() {
        model = new Mat4Identity();

        Vec3D e = new Vec3D(1.5, -5, 2);
        camera = new Camera()
                .withPosition(e)
                .withAzimuth(Math.toRadians(90))
                .withZenith(Math.toRadians(-15));

        projection = new Mat4PerspRH(
                Math.PI / 3,
                obrazBuffer.getHeight() / (float) obrazBuffer.getWidth(),
                0.5,
                50
        );
    }

    private void zobraz() {
        renderer.procisti();

        renderer.setModel(model);
        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(projection);

        renderer.nakresli(castBuffer, indexBuffer, vrcholBuffer);

        // necessary to manually request update of the UI
        panel.repaint();
    }

    private void initListeners(Panel panel) {
        // TODO
    }

}
