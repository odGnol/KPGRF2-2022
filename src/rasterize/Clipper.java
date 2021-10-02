package rasterize;

import model.Point;

import java.util.ArrayList;
import java.util.List;

public class Clipper {

    /**
     * Ořezání obecného polygonu (polygon) jiným konvexním polygonem (clipPolygon)
     *
     * @param polygon     ořezávaný polygon
     * @param clipPolygon ořezávací polygon
     * @return seznam bodů ořezaného polygonu
     */
    public static List<Point> clip(List<Point> polygon, List<Point> clipPolygon) {

        List<Point> in = new ArrayList<>(polygon);

        Point p1 = null; // vložit poslední clip point
        for (Point p2 : clipPolygon) {
            List<Point> out = new ArrayList<>();
            // vytvořit hranu z bodů p1 a p2
            // Point v1 = in.lastx
            for (Point v2 : in) {
                // TODO algoritmus
            }
            p1 = p2;
            in = out;
        }
        return in;
    }

}
