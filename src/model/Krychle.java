package model;

import transforms.Point3D;

public class Krychle extends Teleso {

    public Krychle() {
        vrcholBuffer.add(new Vrchol(new Point3D(-5, -5, -3), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(-3, -5, -3), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(-3, -3, -3), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(-5, -3, -3), nastavitNahodnouBarvu()));

        vrcholBuffer.add(new Vrchol(new Point3D(-5, -5, -5), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(-3, -5, -5), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(-3, -3, -5), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(-5, -3, -5), nastavitNahodnouBarvu()));

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