package model;

import transforms.Point3D;

public class TrojuhelnikovyPas extends Teleso {

    public TrojuhelnikovyPas() {
     vrcholBuffer.add(new Vrchol(new Point3D(1,1,2), nastavitNahodnouBarvu()));
     vrcholBuffer.add(new Vrchol(new Point3D(2,1,2), nastavitNahodnouBarvu()));
     vrcholBuffer.add(new Vrchol(new Point3D(3,1,3), nastavitNahodnouBarvu()));
     vrcholBuffer.add(new Vrchol(new Point3D(1.2,1,2), nastavitNahodnouBarvu()));
     vrcholBuffer.add(new Vrchol(new Point3D(-2,2,3), nastavitNahodnouBarvu()));
     vrcholBuffer.add(new Vrchol(new Point3D(2.5,1.96,3), nastavitNahodnouBarvu()));

     indexBuffer.add(0);
     indexBuffer.add(1);
     indexBuffer.add(2);

     indexBuffer.add(2);
     indexBuffer.add(1);
     indexBuffer.add(3);

     indexBuffer.add(2);
     indexBuffer.add(3);
     indexBuffer.add(4);

     indexBuffer.add(5);
     indexBuffer.add(4);
     indexBuffer.add(6);

     castBuffer.add(new Cast(TypGeometrickeTopologie.TRIANGLE_STRIP, 0, 1));
    }
}
