package control;

import fill.SeedFiller;
import model.Point;
import model.Polygon;
import rasterize.*;
import transforms.Mat3;
import transforms.Mat3Scale2D;
import transforms.Mat3Transl2D;
import transforms.Point2D;
import view.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Controller2D implements Controller {

    private final Panel panel;
    private final Raster raster;

    private LineRasterizer trivialLineRasterizer;
    private SeedFiller seedFiller;

    private int x, y;

    public Controller2D(Panel panel) {
        this.panel = panel;
        this.raster = panel.getRaster();
        initObjects(panel.getRaster());
        initListeners(panel);

//        raster.setPixel(20, 100, 0x00ff00);
//        raster.setPixel(60, 60, Color.GREEN.getRGB());
//
//        for (int y = 100; y <= 300; y++) {
//            raster.setPixel(100, y, 0xffff00);
//        }

        Point2D p1 = new Point2D(20, 50);
        Point2D p2 = new Point2D(200, 70);

        trivialLineRasterizer.rasterize(
                (int) Math.round(p1.getX()),
                (int) Math.round(p1.getY()),
                (int) Math.round(p2.getX()),
                (int) Math.round(p2.getY()),
                Color.YELLOW.getRGB()
        );

//        Mat3 matrix = new Mat3Transl2D(0, 5);

        Mat3 matrix = new Mat3Transl2D(-20, -50)
                .mul(new Mat3Scale2D(2, 2))
                .mul(new Mat3Transl2D(20, 50));
        Point2D a1 = p1.mul(matrix);
        Point2D a2 = p2.mul(matrix);

        trivialLineRasterizer.rasterize(
                (int) Math.round(a1.getX()),
                (int) Math.round(a1.getY()),
                (int) Math.round(a2.getX()),
                (int) Math.round(a2.getY()),
                Color.CYAN.getRGB()
        );

    }

    private void initObjects(Raster raster) {
        trivialLineRasterizer = new TrivialLineRasterizer(raster);

        Polygon polygon = new Polygon();
        polygon.addPoints(new Point(0, 0), new Point(10, 10));

        seedFiller = new SeedFiller(raster);

        //////

//        List<Point> clippedPolygon = Clipper.clip(new ArrayList<>(), new ArrayList<>());
        // rasterizace clippedPolygon
    }

    @Override
    public void initListeners(Panel panel) {
        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isControlDown()) return;

                if (e.isShiftDown()) {
                    //TODO
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    x = e.getX();
                    y = e.getY();
                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    //TODO
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    //TODO
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
//                if (e.isControlDown()) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        //TODO
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        seedFiller.setSeed(new Point(e.getX(), e.getY()));
                        seedFiller.setFillColor(Color.YELLOW.getRGB());
                        seedFiller.fill();
                    }
//                }
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.isControlDown()) return;

                if (e.isShiftDown()) {
                    //TODO
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    raster.clear();
                    trivialLineRasterizer.rasterize(x, y, e.getX(), e.getY(), 0xffffff);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    //TODO
                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    //TODO
                }
                update();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // na klávesu C vymazat plátno
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    //TODO
                }
            }
        });

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects(panel.getRaster());
            }
        });
    }

    private void update() {
//        panel.clear();
        //TODO
    }

    private void hardClear() {
        panel.clear();
    }

}
