package model3d;

import transforms.Point3D;

import java.awt.*;

public class Cube extends Solid {

    public Cube() {
        this(Color.YELLOW.getRGB());
    }

    public Cube(int color) {
        vertexBuffer.add(new Point3D(1, 1, 1)); // 0
        vertexBuffer.add(new Point3D(-1, 1, 1)); // 1
        vertexBuffer.add(new Point3D(-1, -1, 1)); // 2
        vertexBuffer.add(new Point3D(1, -1, 1)); // 3

        vertexBuffer.add(new Point3D(1, 1, -1)); // 4
        vertexBuffer.add(new Point3D(-1, 1, -1)); // 5
        vertexBuffer.add(new Point3D(-1, -1, -1)); // 6
        vertexBuffer.add(new Point3D(1, -1, -1)); // 7

//        indexBuffer.add(0);
//        indexBuffer.add(1);
//        indexBuffer.add(1);
//        indexBuffer.add(2);

        // hrany v horní stěně
        addIndices(0, 1, 1, 2, 2, 3, 3, 0);
        // hrany v dolní stěně
        addIndices(4, 5, 5, 6, 6, 7, 7, 4);
        // prostřední hrany
        addIndices(0, 4, 1, 5, 2, 6, 3, 7);

        this.color = color;
    }

}
