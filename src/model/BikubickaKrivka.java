package model;

import transforms.*;

import java.util.ArrayList;
import java.util.List;

public class BikubickaKrivka extends Teleso {

    public List<Point3D> body = new ArrayList<>();

    Bicubic bikubik;

    public BikubickaKrivka(Mat4 zakladniMatice) {

        body.add(new Point3D(-10, 5, -0.5));
        body.add(new Point3D(2, 5.5, -2));
        body.add(new Point3D(2, 7, -4));
        body.add(new Point3D(3, 14, -25));

        body.add(new Point3D(7, 5, -6.5));
        body.add(new Point3D(1, 20, -10));
        body.add(new Point3D(-2, 10, -9));
        body.add(new Point3D(12, 14, -2.2));

        body.add(new Point3D(3, 2, -13.75));
        body.add(new Point3D(4, 6, -1.25));
        body.add(new Point3D(4, 8, -5.5));
        body.add(new Point3D(2, 16, -4));

        body.add(new Point3D(8, 6, -4));
        body.add(new Point3D(4, 5, -2.4));
        body.add(new Point3D(-7, 8, -5));
        body.add(new Point3D(6, 8, -6.5));


        bikubik = new Bicubic(zakladniMatice, body.toArray(new Point3D[0]), 0);

        for (double i = 0; i <= 1; i += 0.1) {
            for (double j = 0; j <= 1; j += 0.1) {
                vrcholBuffer.add(new Vrchol(bikubik.compute(i, j), nastavitNahodnouBarvu()));
            }
        }

        for (int k = 1; k < vrcholBuffer.size(); k++) {
            indexBuffer.add(k - 1);
            indexBuffer.add(k);
        }

        castBuffer.add(new Cast(TypGeometrickeTopologie.USECKA, 0, indexBuffer.size() / 10));
    }
}
