package fill;

import model.Edge;
import model.Point;
import rasterize.Raster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanLine implements Filler {

    private final Raster raster;
    private final List<Point> points;
    private final int fillColor;
    private final int borderColor;
    // alternativně místo borderColor a points může figurovat Polygon

    public ScanLine(Raster raster, List<Point> points, int fillColor, int borderColor) {
        this.raster = raster;
        this.points = points;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
    }

    @Override
    public void fill() {
        List<Edge> edges = new ArrayList<>();
        // projet všechny body (list points) a vytvořit z nich hrany
        // 0. a 1. bod budou první hranou; 1. a 2. budou druhou hranou; ...; poslední a nultý
        // rovnou ignorovat vodorovné hrany
        // vytvořené nevodorovné hrany zorientovat a přidat do seznamu

        // výsledkem bude seznam zorientovaných hran bez vodorovných úseků

        // najít minimum a maximum pro Y
        int minY = points.get(0).y;
        int maxY = minY;
        // projet všechny body (list points) a najít minimální a maximální Y (optimalizační krok)

        for (int y = minY; y <= maxY; y++) {

            List<Integer> intersections = new ArrayList<>();
            // vnořený cyklus
            // projít všechny hrany (list edges)
            // pokud hrana má průsečík na daném Y...
            // ... tak vypočítat X hodnotu průsečíku a uložit ji do seznamu

            // nyní je naplněný seznam průsečíků

            // setřídit průsečíky
            Collections.sort(intersections);

            // vybarvení mezi průsečíky
            // spojení vždy sudého s lichým (tj. 0. a 1.; 2. a 3.; 4. a 5....)
            // kreslení úseček
            // ??? možná místo rasteru dodat nějaký LineRasterizer
        }

        // obtáhnout hranici tělesa definovaného pomocí listu points
    }
}
