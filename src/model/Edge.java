package model;

public class Edge {

    private int x1, y1, x2, y2;

    // TODO konstruktor

    /**
     * Zjistí, zda je hrana vodorovná
     *
     * @return true pokud je vodorovná, jinak false
     */
    public boolean isHorizontal() {
        // TODO test na rovnost mezi y1 a y2
        return false;
    }

    /**
     * Zorientuje hranu odshora dolů
     */
    public void orientate() {
        // TODO prohození hodnot, pokud y1 je větší než y2
    }

    /**
     * Zjistí, zda existuje průsečík scan-line s touto hranou
     *
     * @param y Y souřadnice vodorovné přímky (scan-line)
     * @return true pokud existuje průsečík
     */
    public boolean hasIntersection(int y) {
        // TODO y, y1,y2 - porovnat, zda je y v rozsahu
        return false;
    }

    /**
     * @param y Y souřadnice vodorovné přímky (scan-line)
     * @return vrátí X souřadnici průsečíku
     */
    public int getIntersection(int y) {
        // TODO vypočítat průsečík pomocí y, k, q (osa Y)
        return 0;
    }

    public boolean isInside(Point p) {
        // slide 23, přednáška C
        // tečný vektor
        Point t = new Point(x2 - x1, y2 - y1);

        // normálový vektor (záleží na orientaci polygonu)
        Point n = new Point(t.y, -t.x);
//        Point n = new Point(-t.y, t.x);

        // vektor k bodu
        Point v = new Point(p.x - x1, p.y - y1);

        return v.x * n.x + v.y * n.y < 0;
    }

    public Point getIntersection(Point p3, Point p4) {
        // slide 25, přednáška C
        // TODO dodělat podle přednášky
        return new Point(0, 0);
    }

}
