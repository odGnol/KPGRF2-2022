package rasterize;

public class TrivialLineRasterizer extends LineRasterizer {

    public TrivialLineRasterizer(Raster raster) {
        super(raster);
    }

    @Override
    public void rasterize(int x1, int y1, int x2, int y2, int color) {
        float k = (y2 - y1) / (float) (x2 - x1);
        float q = y1 - k * x1;
//        System.out.println(k);
        // x1 < x2 - prohodit
        // if (k ..)
        for (int x = x1; x <= x2; x++) {
            float y = k * x + q;
            raster.setPixel(x, Math.round(y), color);
        }
        // else jiná řídící osa
    }
}
