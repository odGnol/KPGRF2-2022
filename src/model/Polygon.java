package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polygon {

    private final List<Point> points;
    private final int color;

    // konstruktor, který umí nastavit jen barvu a výchozí bude prázdný seznam ??

    public Polygon() {
        this(new ArrayList<>());
    }

    public Polygon(List<Point> points) {
        this(points, Color.MAGENTA.getRGB());
    }

    public Polygon(List<Point> points, int color) {
        this.points = points;
        this.color = color;
    }

    public void addPoints(Point... pointsToAdd) { // vararg java
        points.addAll(Arrays.asList(pointsToAdd));
    }

    public void addPoints(List<Point> pointsToAdd) {
        points.addAll(pointsToAdd);
    }

    // clear pro vymazání seznamu vrcholů

//    public void rasterize(LineRasterizer lineRasterizer) {
    //
//    }

}
