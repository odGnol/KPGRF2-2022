package control;

import model.*;
import rasterize.Raster;
import renderer.GPURenderer;
import renderer.Renderer3D;
import transforms.*;
import view.Panel;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controller3D {

    private final Panel panel;
    private final GPURenderer renderer;
    private final Raster<Integer> imageBuffer;

    private Mat4 model, projection;
    private Camera kamera;

    private final List<Teleso> telesaBuffer;

    private final double rychlostSmerPohybu = 0.1;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.imageBuffer = panel.getImageBuffer();
        this.renderer = new Renderer3D(panel.getImageBuffer());

        telesaBuffer = new ArrayList<>();

        initBuffers();
        initMatrices();
        initListeners(panel);

        zobraz();
    }

    private void initBuffers() {
        telesaBuffer.add(new Krychle());
        telesaBuffer.add(new Jehlan());
    }

    private void initMatrices() {
        model = new Mat4Identity();

        Vec3D poziceKamery = new Vec3D(1.5, -5, 2);
        kamera = new Camera()
                .withPosition(poziceKamery)
                .withAzimuth(Math.toRadians(Math.PI * 30))
                .withZenith(Math.toRadians(Math.PI * -4.5))
                .withFirstPerson(false)
                .withRadius(3);

        projection = new Mat4PerspRH(
                Math.PI / 3,
                imageBuffer.getHeight() / (float) imageBuffer.getWidth(),
                0.5,
                50
        );
    }

    private void zobraz() {
        renderer.procisti();

        renderer.setModel(model);
        renderer.setView(kamera.getViewMatrix());
        renderer.setProjection(projection);

        for (int i = 0; i < telesaBuffer.toArray().length; i++) {
            renderer.setModel(telesaBuffer.get(i).getModel());
            renderer.nakresli(telesaBuffer.get(i).getCasti(), telesaBuffer.get(i).getIndexy(), telesaBuffer.get(i).getVrcholy());
        }

        // necessary to manually request update of the UI
        panel.repaint();
    }

    private void initListeners(Panel panel) {

        KeyAdapter kPohybKamery = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                System.out.println("klavesa");
                // pohyb klásesami WSAD + šipky nahoru a dolů
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    kamera = kamera.forward(rychlostSmerPohybu);
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    kamera = kamera.backward(rychlostSmerPohybu);
                } else if (e.getKeyCode() == KeyEvent.VK_A) {
                    kamera = kamera.left(rychlostSmerPohybu);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    kamera = kamera.right(rychlostSmerPohybu);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    kamera = kamera.up(rychlostSmerPohybu);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    kamera = kamera.down(rychlostSmerPohybu);
                }
                zobraz();
            }
        };

        MouseAdapter mPohybKamery = new MouseAdapter() {
            boolean pohybKamery = false;
            int aktualniX, aktualniY = -1;

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    pohybKamery = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pohybKamery = false;
                aktualniX = aktualniY = -1;

            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (pohybKamery) {
                    if (aktualniX != -1 && aktualniY != -1) {
                        kamera = kamera.addAzimuth((aktualniX - e.getX()) / 1000.0);
                        kamera = kamera.addZenith((aktualniY - e.getY()) / 300.0);
                    }
                    zobraz();
                }
                aktualniX = e.getX();
                aktualniY = e.getY();
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() == -1) {
                    Mat4 mat = new Mat4Scale(1.1, 1.1, 1.1);
                    model = model.mul(mat);
                } else {
                    Mat4 mat = new Mat4Scale(0.9, 0.9, 0.9);
                    model = model.mul(mat);
                }
                zobraz();
            }
        };

        panel.addKeyListener(kPohybKamery);
        panel.addMouseListener(mPohybKamery);
        panel.addMouseMotionListener(mPohybKamery);
    }
}