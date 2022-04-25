package model;

import transforms.Point3D;

public class TrojuhelnikovyPas extends Teleso {

    public TrojuhelnikovyPas() {
        for (int i = 1; i < 13; i += 2) {
            vrcholBuffer.add(new Vrchol(new Point3D(i, 5, 2), nastavitNahodnouBarvu()));
            vrcholBuffer.add(new Vrchol(new Point3D(i + 2, 8, 2), nastavitNahodnouBarvu()));
        }

        for (int i = 0; i < 11; i++) {
            indexBuffer.add(i);
        }

        castBuffer.add(new Cast(TypGeometrickeTopologie.TRIANGLE_STRIP, 0, 1));
        castBuffer.add(new Cast(TypGeometrickeTopologie.TRIANGLE_STRIP, 4, 1));
    }
}
