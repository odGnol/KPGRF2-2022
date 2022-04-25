package model;

import transforms.Point3D;

public class TrojuhelnikovyVejir extends Teleso {
    public TrojuhelnikovyVejir() {
        vrcholBuffer.add(new Vrchol(new Point3D(1, 0, 3), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(10, 2, 4), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(4, 10, 4), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(-2, 12, 5), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(-8, 14, 6), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(-14, 16, 7), nastavitNahodnouBarvu()));
        vrcholBuffer.add(new Vrchol(new Point3D(-20, 17, 8), nastavitNahodnouBarvu()));

        for (int i = 0; i < 8; i++) {
            indexBuffer.add(i);
        }

        castBuffer.add(new Cast(TypGeometrickeTopologie.TRIANGLE_FAN, 0, 1));
    }
}
