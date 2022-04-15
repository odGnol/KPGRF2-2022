package model;

import transforms.Col;
import transforms.Point3D;

import java.awt.*;

public class Krychle extends Teleso {
    public Krychle() {
        vrcholBuffer.add(new Vrchol(new Point3D(-1, -1, 1), new Col(1.0, 1.0, 0.0)));
        vrcholBuffer.add(new Vrchol(new Point3D(1, -1, 1), new Col(255, 255, 0)));
        vrcholBuffer.add(new Vrchol(new Point3D(1, 1, 1), new Col(Color.BLUE.getRGB())));
        vrcholBuffer.add(new Vrchol(new Point3D(-1, 1, 1), new Col(Color.GREEN.getRGB())));

        vrcholBuffer.add(new Vrchol(new Point3D(-1, -1, -1), new Col(Color.ORANGE.getRGB())));
        vrcholBuffer.add(new Vrchol(new Point3D(1, -1, -1), new Col(Color.CYAN.getRGB())));
        vrcholBuffer.add(new Vrchol(new Point3D(1, 1, -1), new Col(Color.RED.getRGB())));
        vrcholBuffer.add(new Vrchol(new Point3D(-1, 1, -1), new Col(Color.WHITE.getRGB())));

        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(2);

        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(3);

        indexBuffer.add(1);
        indexBuffer.add(5);
        indexBuffer.add(6);

        indexBuffer.add(1);
        indexBuffer.add(6);
        indexBuffer.add(2);

        indexBuffer.add(6);
        indexBuffer.add(7);
        indexBuffer.add(3);

        indexBuffer.add(6);
        indexBuffer.add(3);
        indexBuffer.add(2);

        indexBuffer.add(7);
        indexBuffer.add(4);
        indexBuffer.add(5);

        indexBuffer.add(7);
        indexBuffer.add(5);
        indexBuffer.add(6);

        indexBuffer.add(0);
        indexBuffer.add(4);
        indexBuffer.add(7);

        indexBuffer.add(0);
        indexBuffer.add(7);
        indexBuffer.add(3);

        indexBuffer.add(0);
        indexBuffer.add(5);
        indexBuffer.add(1);

        indexBuffer.add(0);
        indexBuffer.add(4);
        indexBuffer.add(5);

        castBuffer.add(new Cast(TypGeometrickeTopologie.TROJUHELNIK, 0, 12));
    }
}