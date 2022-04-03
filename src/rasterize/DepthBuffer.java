package rasterize;

import java.util.Optional;

// řeší hloubkovou informaci

public class DepthBuffer implements Raster<Double> {

    private final double[][] zData;
    private double clearValue;

    public DepthBuffer(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new RuntimeException("Zero or negative width or height");
        }

        zData = new double[width][height];

        // výchozí nastavení
        setClearValue(1d);
        clear();
    }

    @Override
    public void clear() {

        // výchozí nastavení, nejdál - 1, nejblíž - 0
        for (double[] z : zData) {
            for (int i = 0; i < z.length; i++) {
                z[i] = clearValue;
            }
            /*
            // IDE nabízí kratší zápis
            Arrays.fill(z, clearValue);
            */
        }
    }

    @Override
    public void setClearValue(Double clearValue) {
        this.clearValue = clearValue;
    }

    @Override
    public int getWidth() {
        return zData.length; // možná naopak s getHeight()
    }

    @Override
    public int getHeight() {
        return zData[0].length; // možná naopak s getWidth()
    }

    @Override
    public Optional<Double> getElement(int x, int y) {
        if (x >= 0 && y >= 0 && x < getWidth() && y < getHeight()) {
            return Optional.of(zData[x][y]);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void setElement(int x, int y, Double zValue) {
        if (x >= 0 && y >= 0 && x < getWidth() && y < getHeight()) {
            zData[x][y] = zValue;
        }
    }
}
