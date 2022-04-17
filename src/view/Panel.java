package view;

import rasterize.Raster;
import rasterize.ImageBuffer;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {

    private final ImageBuffer imageBuffer;

    private static final int SIRKA = 800, VYSKA = 600;

    Panel() {
        setPreferredSize(new Dimension(SIRKA, VYSKA));
        imageBuffer = new ImageBuffer(SIRKA, VYSKA);
        imageBuffer.setClearValue(Color.BLACK.getRGB());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        imageBuffer.repaint(g);
    }

    public Raster<Integer> getImageBuffer() {
        return imageBuffer;
    }

    public void procisti() {
        imageBuffer.clear();
    }
}
