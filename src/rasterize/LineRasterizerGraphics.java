package rasterize;

import java.awt.*;

// nepoužívat pro první a druhou úlohu, lze použít ve třetí úloze
public class LineRasterizerGraphics extends LineRasterizer {

    private final RasterBufferedImage rasterBI;

    public LineRasterizerGraphics(Raster raster) {
        super(raster);
        if (raster instanceof RasterBufferedImage) {
            this.rasterBI = ((RasterBufferedImage) raster);
        } else {
            throw new RuntimeException("LineRasterizerGraphics needs RasterBufferedImage");
        }
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2, int color) {
        Graphics g = rasterBI.getGraphics();
        g.setColor(new Color(color));
        g.drawLine(x1, y1, x2, y2);
    }
}
