package model;

// viz slide 16 - KPGR2/přednášky/PGII_01.pdf
public class Cast {

    // držet vždy jeden typ, neumí dvě zároveň
    private final TypGeometrickeTopologie geometrickyTyp;
    private final int index; // offset? start?
    private final int pocet;

    public Cast(TypGeometrickeTopologie geometrickyTyp, int index, int pocet) {
        this.geometrickyTyp = geometrickyTyp;
        this.index = index;
        this.pocet = pocet;
    }

    public TypGeometrickeTopologie getGeometrickyTyp() {
        return geometrickyTyp;
    }

    public int getIndex() {
        return index;
    }

    public int getPocet() {
        return pocet;
    }

}
