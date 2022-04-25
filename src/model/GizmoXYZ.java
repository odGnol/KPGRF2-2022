package model;

import transforms.Col;
import transforms.Point3D;

public class GizmoXYZ extends Teleso {

    public GizmoXYZ() {
        vrcholBuffer.add(new Vrchol(new Point3D(), new Col(255, 0, 0)));
        vrcholBuffer.add(new Vrchol(new Point3D(), new Col(0, 255, 0)));
        vrcholBuffer.add(new Vrchol(new Point3D(), new Col(0, 0, 255)));
        vrcholBuffer.add(new Vrchol(new Point3D(10, 0, 0), new Col(255, 0, 0)));
        vrcholBuffer.add(new Vrchol(new Point3D(0, 10, 0), new Col(0, 255, 0)));
        vrcholBuffer.add(new Vrchol(new Point3D(0, 0, 10), new Col(0, 0, 255)));

        // hrany šipky x
        vrcholBuffer.add(new Vrchol(new Point3D(7, 2, 0), new Col(255, 0, 0)));
        vrcholBuffer.add(new Vrchol(new Point3D(7, -2, 0), new Col(255, 0, 0)));

        // hrany šipky y
        vrcholBuffer.add(new Vrchol(new Point3D(2, 7, 0), new Col(0, 255, 0)));
        vrcholBuffer.add(new Vrchol(new Point3D(-2, -7, 0), new Col(0, 255, 0)));

        // hrany šipky z
        vrcholBuffer.add(new Vrchol(new Point3D(2, 0, 7), new Col(0, 0, 255)));
        vrcholBuffer.add(new Vrchol(new Point3D(-2, 0, 7), new Col(0, 0, 255)));

        indexBuffer.add(0);
        indexBuffer.add(3);
        indexBuffer.add(1);
        indexBuffer.add(4);
        indexBuffer.add(2);
        indexBuffer.add(5);

        // šipka x
        indexBuffer.add(3);
        indexBuffer.add(6);
        indexBuffer.add(3);
        indexBuffer.add(7);

        // šipka y
        indexBuffer.add(4);
        indexBuffer.add(8);
        indexBuffer.add(4);
        indexBuffer.add(9);

        // šipka z
        indexBuffer.add(5);
        indexBuffer.add(10);
        indexBuffer.add(5);
        indexBuffer.add(11);

        castBuffer.add(new Cast(TypGeometrickeTopologie.USECKA, 0, 9));
    }
}
