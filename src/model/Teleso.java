package model;

import transforms.Bicubic;
import transforms.Col;
import transforms.Mat4;
import transforms.Mat4Identity;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class Teleso {

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

    public Col nastavitNahodnouBarvu() {
        final Random nahodne = new Random();

        double R = nahodne.nextDouble();
        double G = nahodne.nextDouble() / 2f + 0.5;
        double B = nahodne.nextDouble() / 2f + 0.5;

        return new Col(R, G, B);
    }
}