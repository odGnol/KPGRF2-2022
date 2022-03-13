package view;

import rasterize.Raster;
import rasterize.ObrazBuffer;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {

    private final ObrazBuffer imageBuffer;

    private static final int SIRKA = 800, VYSKA = 600;

    Panel() {
        setPreferredSize(new Dimension(SIRKA, VYSKA));
        imageBuffer = new ObrazBuffer(SIRKA, VYSKA);
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

}
