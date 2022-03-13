package rasterize;

import java.util.Optional;

public class DepthBuffer implements Raster<Double> {

    private final double[][] zData;
    private double clearValue;

    public DepthBuffer(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new RuntimeException("Zero or negative width or height");
        }
        zData = new double[width][height];

        setClearValue(1d);
        clear();
    }

    @Override
    public void clear() {
        for (double[] z : zData) {
            for (int i = 0; i < z.length; i++) {
                z[i] = clearValue;
            }
            /*
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
        return Optional.of(zData[x][y]);
    }

    @Override
    public void setElement(int x, int y, Double value) {
        zData[x][y] = value;
    }
}
