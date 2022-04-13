package control;

import model.Cast;
import model.Krychle;
import model.Objekt;
import model.Vrchol;
import rasterize.Raster;
import renderer.GPURenderer;
import renderer.Renderer3D;
import transforms.*;
import view.Panel;

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
    private final List<Objekt> objektBuffer;

    private Krychle krychle;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.obrazBuffer = panel.getImageBuffer();
        this.renderer = new Renderer3D(panel.getImageBuffer());

        objektBuffer = new ArrayList<>();

        initBuffers();
        initMatrices();
        initListeners(panel);

        zobraz();
    }

    private void initBuffers() {
        objektBuffer.add(new Krychle());
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

        for (int i = 0; i < objektBuffer.toArray().length; i++) {
            renderer.setModel(objektBuffer.get(i).getModel());
            renderer.nakresli(objektBuffer.get(i).getCasti(), objektBuffer.get(i).getIndexy(), objektBuffer.get(i).getVrcholy());
        }

        // necessary to manually request update of the UI
        panel.repaint();
    }

    private void initListeners(Panel panel) {
        // TODO
    }

}
