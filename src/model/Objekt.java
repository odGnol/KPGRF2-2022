package model;

import transforms.Mat4;
import transforms.Mat4Identity;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Objekt {

    //seznam vrcholů - Point3D
    final List<Vrchol> vrcholBuffer = new ArrayList<>();

    //seznam indexovaných vrcholů
    final List<Integer> indexBuffer = new ArrayList<>();

    //seznam částí
    final List<Cast> castBuffer = new ArrayList<>();

    Mat4 model = new Mat4Identity();

    Color barva;

    public Mat4 getModel() {
        return model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    public List<Cast> getCasti() {
        return castBuffer;
    }

    public List<Vrchol> getVrcholy() {
        return vrcholBuffer;
    }

    public List<Integer> getIndexy() {
        return indexBuffer;
    }

    public Color getBarva() {
        return barva;
    }

    public void setBarva(Color barva) {
        this.barva = barva;
    }

}