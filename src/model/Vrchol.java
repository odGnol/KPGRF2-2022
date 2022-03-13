package model;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.Optional;

public class Vrchol {

    // ukládá umístění vrcholu a barvu
    private final Point3D bod;
    private final Col barva;

    public Vrchol(Point3D bod, Col barva) {
        this.bod = bod;
        this.barva = barva;
    }

    public Vrchol mul(double t) {
        return new Vrchol(bod.mul(t), barva.mul(t));
    }

    public Vrchol add(Vrchol v) {
        return new Vrchol(bod.add(v.getBod()), barva.add(v.getBarva()));
    }

    public Optional<Vrchol> dehomog() {
        Optional<Vec3D> optional = bod.dehomog();
        if (optional.isPresent()) {
            return Optional.of(new Vrchol(new Point3D(optional.get()), barva));
        } else {
            return Optional.empty();
        }
    }

    public Point3D getBod() {
        return bod;
    }

    public double getX() {
        return getBod().getX();
    }

    public double getY() {
        return getBod().getY();
    }

    public double getZ() {
        return getBod().getZ();
    }

    public double getW() {
        return getBod().getW();
    }

    public Col getBarva() {
        return barva;
    }

}
