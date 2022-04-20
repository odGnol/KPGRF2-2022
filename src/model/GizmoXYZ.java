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

        indexBuffer.add(0);
        indexBuffer.add(3);
        indexBuffer.add(1);
        indexBuffer.add(4);
        indexBuffer.add(2);
        indexBuffer.add(5);

        castBuffer.add(new Cast(TypGeometrickeTopologie.USECKA, 0, 3));
    }
}
