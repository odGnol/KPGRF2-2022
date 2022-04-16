package model;

import transforms.Point3D;

import java.util.ArrayList;

public class Jehlan extends Teleso {
    public Jehlan() {

        vrcholBuffer.add(new Vrchol(new Point3D(1,1,1), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(-1,-1,1), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(-1,1,-1), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(1,-1,-1), nastavitNahodnouBarvu()));

        indexBuffer.add(0);
        indexBuffer.add(1);
        indexBuffer.add(2);

        indexBuffer.add(0);
        indexBuffer.add(2);
        indexBuffer.add(3);

        indexBuffer.add(0);
        indexBuffer.add(3);
        indexBuffer.add(1);

        indexBuffer.add(3);
        indexBuffer.add(2);
        indexBuffer.add(1);

        castBuffer.add(new Cast(TypGeometrickeTopologie.TROJUHELNIK, 0, 4));
    }
}
