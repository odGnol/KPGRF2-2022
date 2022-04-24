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

    private Mat4 model, projection, maticeTransformace;
    private Camera kamera;
    private Mat4 typMaticeKrivky = Cubic.BEZIER;

    private final List<Teleso> telesaBuffer;
    private final List<Teleso> krivkyBuffer;
    private final List<Teleso> xyzOsyBuffer;

    private final double rychlostSmerPohybu = 0.1;
    private boolean rotace = false;
    private boolean translace = false;
    private boolean meritko = false;
    private boolean jeDratovy = false;
    private int poziceTelesa = 0;
    private boolean transformaceKrivek = false;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.imageBuffer = panel.getImageBuffer();
        this.renderer = new Renderer3D(panel.getImageBuffer());

        telesaBuffer = new ArrayList<>();
        krivkyBuffer = new ArrayList<>();
        xyzOsyBuffer = new ArrayList<>();

        initBuffers();
        initMatrices();
        initListeners(panel);

        zobraz();
    }

    private void initBuffers() {
        telesaBuffer.add(new Krychle());
        telesaBuffer.add(new Jehlan());
        telesaBuffer.add(new KomolyJehlan());
        krivkyBuffer.add(new BikubickaKrivka(typMaticeKrivky));
        xyzOsyBuffer.add(new GizmoXYZ());
    }

    private void initMatrices() {
        model = new Mat4Identity();

        Vec3D poziceKamery = new Vec3D(2, -12, 4);
        kamera = new Camera()
                .withPosition(poziceKamery)
                .withAzimuth(Math.toRadians(Math.PI * 25))
                .withZenith(Math.toRadians(Math.PI * -8.5))
                .withFirstPerson(false)
                .withRadius(5);

        projection = new Mat4PerspRH(
                Math.PI / 3,
                imageBuffer.getHeight() / (float) imageBuffer.getWidth(),
                0.5,
                50
        );
    }

    private void zobraz() {
        panel.procisti();
        renderer.procisti();

        renderer.setModel(model);
        renderer.setPohled(kamera.getViewMatrix());
        renderer.setProjekce(projection);

        for (int i = 0; i < telesaBuffer.toArray().length; i++) {
            renderer.setModel(telesaBuffer.get(i).getModel());
            renderer.nakresli(telesaBuffer.get(i).getCasti(), telesaBuffer.get(i).getIndexy(), telesaBuffer.get(i).getVrcholy());
        }

        renderer.setModel(krivkyBuffer.get(0).getModel());
        renderer.nakresli(krivkyBuffer.get(0).getCasti(), krivkyBuffer.get(0).getIndexy(), krivkyBuffer.get(0).getVrcholy());

        renderer.setModel(xyzOsyBuffer.get(0).getModel());
        renderer.nakresli(xyzOsyBuffer.get(0).getCasti(), xyzOsyBuffer.get(0).getIndexy(), xyzOsyBuffer.get(0).getVrcholy());

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

    private void nastavDratoveZobrazeni(boolean jeDratovy) {
        renderer.procisti();
        renderer.setPohled(kamera.getViewMatrix());
        renderer.setDratovyModel(jeDratovy);
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

        KeyAdapter vyberTransformaceTypuTeles = new KeyAdapter() {
            public void keyPressed(KeyEvent udalost) {
                var klavesa = udalost.getKeyCode();
                if (klavesa == KeyEvent.VK_I) {
                    System.out.print("Tělesa - Komol jehlan, jehlan, krychle.");
                    transformaceKrivek = false;
                } else if (klavesa == KeyEvent.VK_O) {
                    System.out.print("Křivky - kubika");
                    transformaceKrivek = true;
                }
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
        };

        KeyAdapter kVyberTransformaci = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent udalost) {
                var klavesa = udalost.getKeyCode();
                if (klavesa == KeyEvent.VK_1) {
                    System.out.println("Rotace.");
                    rotace = true;
                    translace = meritko = false;
                } else if (klavesa == KeyEvent.VK_2) {
                    System.out.println("Translace.");
                    translace = true;
                    rotace = meritko = false;
                } else if (klavesa == KeyEvent.VK_3) {
                    System.out.println("Meritko.");
                    meritko = true;
                    rotace = translace = false;
                }
            }
        };

        KeyAdapter kZmenaTelesa = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent udalost) {
                var klavesa = udalost.getKeyCode();

                if (transformaceKrivek && (klavesa == KeyEvent.VK_G || klavesa == KeyEvent.VK_F)) {
                    System.out.println("Přepněte na další typ křivky nebo změňte transformaci těles.");
                } else {
                    if (klavesa == KeyEvent.VK_G) {
                        if (poziceTelesa == telesaBuffer.size() - 1) {
                            poziceTelesa = 0;
                        } else {
                            poziceTelesa++;
                        }
                    } else if (klavesa == KeyEvent.VK_F) {
                        if (poziceTelesa == 0) {
                            poziceTelesa = telesaBuffer.size() - 1;
                        } else {
                            poziceTelesa--;
                        }
                    }
                }
            }
        };

        KeyAdapter kNastavDratoveZobrazeni = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent udalost) {
                var klavesa = udalost.getKeyCode();

                if (klavesa == KeyEvent.VK_R) {
                    if (jeDratovy) {
                        nastavDratoveZobrazeni(false);
                        jeDratovy = false;
                    } else {
                        nastavDratoveZobrazeni(true);
                        jeDratovy = true;
                    }
                }
            }
        };

        KeyAdapter kVyberTypuKBikupickychrivek = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent udalost) {
                var klavesa = udalost.getKeyCode();
                if (klavesa == KeyEvent.VK_B) {
                    System.out.println("Bézierova kubika");

                    krivkyBuffer.clear();
                    typMaticeKrivky = Cubic.BEZIER;
                    krivkyBuffer.add(new BikubickaKrivka(Cubic.BEZIER));
                } else if (klavesa == KeyEvent.VK_N) {
                    System.out.println("Coonsova kubika");

                    krivkyBuffer.clear();
                    typMaticeKrivky = Cubic.COONS;
                    krivkyBuffer.add(new BikubickaKrivka(Cubic.COONS));
                } else if (klavesa == KeyEvent.VK_M) {
                    System.out.println("Fergusonova kubika");

                    krivkyBuffer.clear();
                    typMaticeKrivky = Cubic.FERGUSON;
                    krivkyBuffer.add(new BikubickaKrivka(Cubic.FERGUSON));
                }
                zobraz();
            }
        };

        MouseAdapter mTransformaceTelesa = new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent udalost) {
                if (udalost.getWheelRotation() < 0) {
                    if (translace) {
                        maticeTransformace = new Mat4Transl(1.1, 0, 0);
                    } else if (rotace) {
                        maticeTransformace = new Mat4RotXYZ(0, 0, 1.1);
                    } else if (meritko) {
                        maticeTransformace = new Mat4Scale(1.1, 1.1, 1.1);
                    } else {
                        maticeTransformace = new Mat4Scale(1.1, 1.1, 1.1);
                    }

                    if (transformaceKrivek) {
                        krivkyBuffer.get(0).setModel(krivkyBuffer.get(0).getModel().mul(maticeTransformace));
                    } else {
                        telesaBuffer.get(poziceTelesa).setModel(telesaBuffer.get(poziceTelesa).getModel().mul(maticeTransformace));
                    }
                } else {
                    if (translace) {
                        maticeTransformace = new Mat4Transl(-1.1, 0, 0);
                    } else if (rotace) {
                        maticeTransformace = new Mat4RotXYZ(0, 0, -1.1);
                    } else if (meritko) {
                        maticeTransformace = new Mat4Scale(0.9, 0.9, 0.9);
                    } else {
                        maticeTransformace = new Mat4Scale(0.9, 0.9, 0.9);
                    }

                    if (transformaceKrivek) {
                        krivkyBuffer.get(0).setModel(krivkyBuffer.get(0).getModel().mul(maticeTransformace));
                    } else {
                        telesaBuffer.get(poziceTelesa).setModel(telesaBuffer.get(poziceTelesa).getModel().mul(maticeTransformace));
                    }

                }
                zobraz();
            }
        };

        panel.addKeyListener(kPohybKamery);
        panel.addKeyListener(projekce);
        panel.addKeyListener(kZmenaTelesa);
        panel.addKeyListener(kVyberTransformaci);
        panel.addKeyListener(vyberTransformaceTypuTeles);
        panel.addKeyListener(kNastavDratoveZobrazeni);
        panel.addKeyListener(kVyberTypuKBikupickychrivek);
        panel.addMouseListener(mPohybKamery);
        panel.addMouseMotionListener(mPohybKamery);
        panel.addMouseWheelListener(mTransformaceTelesa);
    }
}