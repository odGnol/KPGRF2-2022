package fill;

import model.Point;
import rasterize.Raster;

public class SeedFiller implements Filler {

    private final Raster raster;
    private int seedX, seedY;
    private int backgroundColor;
    private int fillColor;

    public SeedFiller(Raster raster) {
        this.raster = raster;
    }

    public void setSeed(Point seed) {
        seedX = seed.x;
        seedY = seed.y;
        backgroundColor = raster.getPixel(seedX, seedY);
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    @Override
    public void fill() {
        seed(seedX, seedY);
    }

    private void seed(int x, int y) {
        // VM options -Xss40m
        // podmínka na velikost plátna
        if (backgroundColor == raster.getPixel(x, y)) {
            raster.setPixel(x, y, fillColor);
            seed(x + 1, y); // doprava
            seed(x - 1, y); // doleva
            seed(x, y + 1); // dolů
            seed(x, y - 1); // nahoru
        }
    }
}
