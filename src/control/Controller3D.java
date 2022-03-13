package control;

import model.Part;
import model.TopologyType;
import model.Vertex;
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
    private final Raster<Integer> imageBuffer;

    private Mat4 model, projection;
    private Camera camera;

    private List<Vertex> vertexBuffer;
    private List<Integer> indexBuffer;
    private List<Part> partBuffer;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.imageBuffer = panel.getImageBuffer();
        this.renderer = new Renderer3D(panel.getImageBuffer());

        initBuffers();
        initMatrices();
        initListeners(panel);

        // test draw
//        imageBuffer.setElement(50, 50, Color.YELLOW.getRGB());
//        panel.repaint();

        display();
    }

    private void initBuffers() {
        vertexBuffer = new ArrayList<>();
        vertexBuffer.add(new Vertex(new Point3D(-1, -1, 1), new Col(1.0, 1.0, 0.0)));
        vertexBuffer.add(new Vertex(new Point3D(1, -1, 1), new Col(255, 255, 0)));
        vertexBuffer.add(new Vertex(new Point3D(1, 1, 1), new Col(Color.BLUE.getRGB())));
        vertexBuffer.add(new Vertex(new Point3D(-1, 1, 1), new Col(Color.GREEN.getRGB())));

        vertexBuffer.add(new Vertex(new Point3D(-1, -1, -1), new Col(Color.ORANGE.getRGB())));
        vertexBuffer.add(new Vertex(new Point3D(1, -1, -1), new Col(Color.CYAN.getRGB())));
        vertexBuffer.add(new Vertex(new Point3D(1, 1, -1), new Col(Color.RED.getRGB())));
        vertexBuffer.add(new Vertex(new Point3D(-1, 1, -1), new Col(Color.WHITE.getRGB())));

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

        partBuffer = new ArrayList<>();
        partBuffer.add(new Part(TopologyType.TRIANGLE, 0, 2)); // FIXME count bude 12 až bude celý IB
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
                imageBuffer.getHeight() / (float) imageBuffer.getWidth(),
                0.5,
                50
        );
    }

    private void display() {
        renderer.clear();

        renderer.setModel(model);
        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(projection);

        renderer.draw(partBuffer, indexBuffer, vertexBuffer);

        // necessary to manually request update of the UI
        panel.repaint();
    }

    private void initListeners(Panel panel) {
        // TODO
    }

}
