package model;

import transforms.Point3D;

public class Jehlan extends Teleso {
    public Jehlan() {

        vrcholBuffer.add(new Vrchol(new Point3D(1.5,5,4), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(-1.5,-1,4), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(-1.5,5,-2), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(1.5,-1,-2), nastavitNahodnouBarvu()));

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
