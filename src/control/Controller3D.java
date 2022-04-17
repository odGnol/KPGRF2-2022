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
        telesaBuffer.add(new KomolyJehlan());
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
        renderer.setPohled(kamera.getViewMatrix());
        renderer.setProjekce(projection);

        for (int i = 0; i < telesaBuffer.toArray().length; i++) {
            renderer.setModel(telesaBuffer.get(i).getModel());
            renderer.nakresli(telesaBuffer.get(i).getCasti(), telesaBuffer.get(i).getIndexy(), telesaBuffer.get(i).getVrcholy());
        }

        // necessary to manually request update of the UI
        panel.repaint();
    }

    private void nastavNovouProjekci() {
        renderer.procisti();
        renderer.setPohled(kamera.getViewMatrix());

        if (projection instanceof Mat4PerspRH) {
            // ortogonální projekce
            projection = new Mat4OrthoRH(5, 5, 0.5, 30);
        } else {
            // perspektivní projekce
            projection = new Mat4PerspRH(Math.PI / 3, imageBuffer.getHeight() / (float) imageBuffer.getWidth(), 0.5, 30);
        }

        renderer.setProjekce(projection);
        renderer.setModel(model);
        zobraz();
    }

    private void initListeners(Panel panel) {

        KeyAdapter kPohybKamery = new KeyAdapter() {
            public void keyPressed(KeyEvent udalost) {
                var klavesa = udalost.getKeyCode();
                // pohyb klásesami WSAD + šipky nahoru a dolů
                if (klavesa == KeyEvent.VK_W) {
                    kamera = kamera.forward(rychlostSmerPohybu);
                } else if (klavesa == KeyEvent.VK_S) {
                    kamera = kamera.backward(rychlostSmerPohybu);
                } else if (klavesa == KeyEvent.VK_A) {
                    kamera = kamera.left(rychlostSmerPohybu);
                } else if (klavesa == KeyEvent.VK_D) {
                    kamera = kamera.right(rychlostSmerPohybu);
                } else if (klavesa == KeyEvent.VK_UP) {
                    kamera = kamera.up(rychlostSmerPohybu);
                } else if (klavesa == KeyEvent.VK_DOWN) {
                    kamera = kamera.down(rychlostSmerPohybu);
                }
                zobraz();
            }
        };

        KeyAdapter projekce = new KeyAdapter() {
            public void keyPressed(KeyEvent udalost) {
                var klavesa = udalost.getKeyCode();
                if (klavesa == KeyEvent.VK_P) {
                    nastavNovouProjekci();
                }
            }
        };

        MouseAdapter mPohybKamery = new MouseAdapter() {
            // pohyb myší
            boolean pohybKamery = false;
            int aktualniX, aktualniY = -1;

            @Override
            public void mousePressed(MouseEvent udalost) {
                var mys = udalost.getButton();
                if (mys == MouseEvent.BUTTON1) {
                    pohybKamery = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent udalost) {
                pohybKamery = false;
                aktualniX = aktualniY = -1;
            }

            @Override
            public void mouseDragged(MouseEvent udalost) {
                if (pohybKamery) {
                    if (aktualniX != -1 && aktualniY != -1) {
                        kamera = kamera.addAzimuth((aktualniX - udalost.getX()) / 1000.0);
                        kamera = kamera.addZenith((aktualniY - udalost.getY()) / 300.0);
                    }
                    zobraz();
                }
                aktualniX = udalost.getX();
                aktualniY = udalost.getY();
            }

            // FIXME nefunkční scale
            @Override
            public void mouseWheelMoved(MouseWheelEvent udalost) {
                if (udalost.getWheelRotation() < 0) {
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
        panel.addKeyListener(projekce);
        panel.addMouseListener(mPohybKamery);
        panel.addMouseMotionListener(mPohybKamery);
        panel.addMouseWheelListener(mPohybKamery);
    }
}