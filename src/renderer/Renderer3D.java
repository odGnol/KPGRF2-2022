package renderer;

import model.Cast;
import model.TypGeometrickeTopologie;
import model.Vrchol;
import rasterize.DepthBuffer;
import rasterize.Raster;
import transforms.*;

import java.util.List;
import java.util.Optional;

public class Renderer3D implements GPURenderer {

    private final Raster<Integer> imageBuffer;
    private final DepthBuffer depthBuffer;

    private Mat4 model, pohled, projekce;

    public Renderer3D(Raster<Integer> raster) {
        this.imageBuffer = raster;
        depthBuffer = new DepthBuffer(raster.getWidth(), raster.getHeight());

        model = new Mat4Identity();
        pohled = new Mat4Identity();
        projekce = new Mat4Identity();
    }

    @Override
    public void nakresli(List<Cast> casti, List<Integer> ib, List<Vrchol> vb) {
        for (Cast cast : casti) {
            TypGeometrickeTopologie topologyType = cast.getGeometrickyTyp();
            int index = cast.getIndex();
            int pocet = cast.getPocet();

            if (topologyType == TypGeometrickeTopologie.TROJUHELNIK) {
                for (int i = index; i < pocet * 3 + index; i += 3) {
                    Integer i1 = ib.get(i);
                    Integer i2 = ib.get(i + 1);
                    Integer i3 = ib.get(i + 2);

                    Vrchol v1 = vb.get(i1);
                    Vrchol v2 = vb.get(i2);
                    Vrchol v3 = vb.get(i3);
                    pripravTrojuhelnik(v1, v2, v3);
                }
            } else if (topologyType == TypGeometrickeTopologie.USECKA) {
                // Doplnění: úsečka
                for (int i = index; i < pocet * 2; i += 2) {
                    Integer i1 = ib.get(i);
                    Integer i2 = ib.get(i + 1);

                    Vrchol v1 = vb.get(i1);
                    Vrchol v2 = vb.get(i2);
                    pripravUsecku(v1, v2);
                }
            } else if (topologyType == TypGeometrickeTopologie.BOD) {
                for (int i = index; i < pocet; i++) {
                    Integer i1 = ib.get(i);

                    Vrchol v1 = vb.get(i1);

//                    pripravBod(v1);
                    nakresliPixel((int) v1.getX(), (int) v1.getY(), v1.getZ(), v1.getBarva());
                }
            } // ...
        }
    }

    private void pripravTrojuhelnik(Vrchol v1, Vrchol v2, Vrchol v3) {
        // 1. transformace vrcholů
        Vrchol a = new Vrchol(
                v1.getBod().mul(model).mul(pohled).mul(projekce),
                v1.getBarva()
        );
        Vrchol b = new Vrchol(
                v2.getBod().mul(model).mul(pohled).mul(projekce),
                v2.getBarva()
        );
        Vrchol c = new Vrchol(
                v3.getBod().mul(model).mul(pohled).mul(projekce),
                v3.getBarva()
        );

        // 2. ořezání
        // ořezat trojúhelníky, které jsou CELÉ mimo zobrazovací objem
        // slides 86 a 97

        // podle podle osy x
        if (a.getX() > a.getW() && b.getX() > b.getW() && c.getX() > c.getW()) return; // trojúhelník je moc vpravo
        if (a.getX() < -a.getW() && b.getX() < -b.getW() && c.getX() < -c.getW()) return; // moc vlevo

        // podle osy y
        if (a.getY() > a.getW() && b.getY() > b.getW() && c.getY() > c.getW()) return; // moc nahoře
        if (a.getY() < -a.getW() && b.getY() < -b.getW() && c.getY() < -c.getW()) return; // moc dole

        // podle osy z
        if (a.getZ() > a.getW() && b.getZ() > b.getW() && c.getZ() > c.getW()) return; // moc vzadu/daleko (?)
        if (a.getZ() < 0 && b.getZ() < 0 && c.getZ() < 0) return; // je za námi

        // 3. seřazení podle Z
        // slide 101
        // od javy 11 je možné využít var proměnnou, jen pro lokální proměnnou: var docasne = a;
        if (a.getZ() < b.getZ()) {
            var docasne = a;
            a = b;
            b = docasne;
        }
        if (b.getZ() < c.getZ()) {
            var docasne = b;
            b = c;
            c = docasne;
        }
        // teď je v C vrchol, který je nám nejblíže
        if (a.getZ() < b.getZ()) {
            var docasne = a;
            a = b;
            b = docasne;
        }

        // teď máme seřazeno - Z je od největšího po nejmenší: A, B, C

        // 4. ořezání podle hrany Z
        // slide 101
        // lineární interpolace
        if (a.getZ() < 0) {
            // A.Z je menší než nula = > všechny Z jsou menší než nula => není co zobrazit
        } else if (b.getZ() < 0) {
            // vrchol A je vidět, vrcholy B a C nejsou
            // odečíst minimum: 0 - a.getZ() | dělit rozsahem: b.getZ() - a.getZ()
            double t1 = (0 - a.getZ()) / (b.getZ() - a.getZ());
            // 0 -> protože ten nový vrchol (ab), který má vzniknout, bude mít souřadnici Z nula
            Vrchol ab = a.mul(1 - t1).add(b.mul(t1));

            double t2 = -a.getZ() / (c.getZ() - a.getZ());
            Vrchol ac = a.mul(1 - t2).add(c.mul(t2));

            nakresliTrojuhelnik(a, ab, ac);

        } else if (c.getZ() < 0) {
            // 2 krát se zavolá funkce nakresliTrojuhelnik()

            double t1 = -a.getZ() / c.getZ() - a.getZ();
            Vrchol ac = a.mul(1 - t1).add(c.mul(t1));

            double t2 = -b.getZ() / c.getZ() - b.getZ();
            Vrchol bc = b.mul(1 - t2).add(c.mul(t2));

            nakresliTrojuhelnik(a, b, bc);
            nakresliTrojuhelnik(a, ac, bc);
        } else {
            // vidíme celý trojúhelník
            nakresliTrojuhelnik(a, b, c);
        }
    }

    private void nakresliTrojuhelnik(Vrchol a, Vrchol b, Vrchol c) {
        // 1. dehomogenizace
        Optional<Vrchol> dA = a.dehomog();
        Optional<Vrchol> dB = b.dehomog();
        Optional<Vrchol> dC = c.dehomog();

        // zahodit trojúhelník, pokud některý vrchol má w==0 (nelze provést dehomogenizaci)
        if (dA.isEmpty() || dB.isEmpty() || dC.isEmpty()) return;

        // 2. transformace do okna
        Vec3D vec3D1 = transformujDoOkna(dA.get());
        Vrchol v1 = vytvorVrcholZVektoruABarvy(vec3D1, dA.get().getBarva());

        Vec3D vec3D2 = transformujDoOkna(dB.get());
        Vrchol v2 = vytvorVrcholZVektoruABarvy(vec3D2, dB.get().getBarva());

        Vec3D vec3D3 = transformujDoOkna(dC.get());
        Vrchol v3 = vytvorVrcholZVektoruABarvy(vec3D3, dC.get().getBarva());

        // 3. seřazení podle Y
        if (v1.getY() > v2.getY()) {
            var docasne = v1;
            v1 = v2;
            v2 = docasne;
        }
        if (v2.getY() > v3.getY()) {
            var docasne = v2;
            v2 = v3;
            v3 = docasne;
        }
        if (v1.getY() > v2.getY()) {
            var docasne = v1;
            v1 = v2;
            v2 = docasne;
        }

        // 4. interpolace podle Y
        // slides 129, 130
        // 1. for cyklus A->B
        int startAB = Math.max(0, (int) v1.getY() + 1);
        double endAB = Math.min(imageBuffer.getHeight() - 1, v2.getY());

        for (int y = startAB; y <= endAB; y++) {
            double t1 = (y - v1.getY()) / (v2.getY() - v1.getY());
            Vrchol d = v1.mul(1 - t1).add(v2.mul(t1));

            double t2 = (y - v1.getY()) / (v3.getY() - v1.getY());
            Vrchol e = v1.mul(1 - t2).add(v3.mul(t2));

            naplnUsecku(d, e);
        }
        // 2. for cyklus B->C
        // Doplnění: cyklus interpolace po ose X

        int startBC = Math.max(0, (int) v2.getY() + 1);
        double endBC = Math.min(imageBuffer.getWidth() - 1, v3.getY());

        for (int y = startBC; y <= endBC; y++) {
            double t3 = (y - v2.getY()) / (v3.getY() - v2.getY());
            Vrchol f = v2.mul(1 - t3).add(v3.mul(t3));

            double t4 = (y - v1.getY()) / (v3.getY() - v1.getY());
            Vrchol g = v1.mul(1 - t4).add(v3.mul(t4));

            naplnUsecku(f, g);
        }
    }

    private void pripravUsecku(Vrchol v1, Vrchol v2) {
        // 1. transformace vrcholů
        Vrchol a = new Vrchol(
                v1.getBod().mul(model).mul(pohled).mul(projekce),
                v1.getBarva()
        );
        Vrchol b = new Vrchol(
                v2.getBod().mul(model).mul(pohled).mul(projekce),
                v2.getBarva()
        );

        // 2. ořezání

        // podle podle osy x
        if (a.getX() > a.getW() && b.getX() > b.getW()) return; // úsečka je moc vpravo
        if (a.getX() < -a.getW() && b.getX() < -b.getW()) return; // moc vlevo

        // podle osy y
        if (a.getY() > a.getW() && b.getY() > b.getW()) return; // moc nahoře
        if (a.getY() < -a.getW() && b.getY() < -b.getW()) return; // moc dole

        // podle osy z
        if (a.getZ() > a.getW() && b.getZ() > b.getW()) return; // moc vzadu/daleko (?)
        if (a.getZ() < 0 && b.getZ() < 0) return; // je za námi

        // 3. seřazení podle Z
        if (a.getZ() < b.getZ()) {
            var docasne = a;
            a = b;
            b = docasne;
        }

        // 4. ořezání podle hrany Z
        if (a.getZ() < 0) {
            // A.Z je menší než nula = > všechny Z jsou menší než nula => není co zobrazit
        } else if (b.getZ() < 0) {
            // vrchol A je vidět, vrchol B není vidět
            // odečíst minimum: 0 - a.getZ() | dělit rozsahem: b.getZ() - a.getZ()
            double t1 = (0 - a.getZ()) / (b.getZ() - a.getZ());
            Vrchol ab = a.mul(1 - t1).add(b.mul(t1));

            nakresliUsecku(a, ab);
        } else {
            // vidíme celou úsečku
            nakresliUsecku(a, b);
        }
    }

    private void nakresliUsecku(Vrchol a, Vrchol b) {
        // 1. dehomogenizace
        Optional<Vrchol> dA = a.dehomog();
        Optional<Vrchol> dB = b.dehomog();

        // zahodit trojúhelník, pokud některý vrchol má w==0 (nelze provést dehomogenizaci)
        if (dA.isEmpty() || dB.isEmpty()) return;

        // 2. transformace do okna
        Vec3D vec3D1 = transformujDoOkna(dA.get());
        Vrchol v1 = vytvorVrcholZVektoruABarvy(vec3D1, dA.get().getBarva());

        Vec3D vec3D2 = transformujDoOkna(dB.get());
        Vrchol v2 = vytvorVrcholZVektoruABarvy(vec3D2, dB.get().getBarva());

        var x1 = v1.getX();
        var y1 = v1.getY();
        var x2 = v2.getX();
        var y2 = v2.getX();
        var dx = x2 - x1;
        var dy = y2 - y1;

        if (Math.abs(dy) < Math.abs(dx)) {
            if (x2 < x1) {
                var docasne = v1;
                v1 = v2;
                v2 = docasne;
            }

            // 2. interpolace po ose X
            int xStart = Math.max(0, (int) v1.getX() + 1);
            double xEnd = Math.min(imageBuffer.getWidth() - 1, v2.getX());

            for (int x = xStart; x <= xEnd; x++) {
                double t1 = (x - v1.getX()) / (v2.getX() - v1.getX());
                Vrchol ff = v1.mul(1 - t1).add(v2.mul(t1));
                nakresliPixel((int) Math.round(ff.getX()), (int) Math.round(ff.getY()), ff.getZ(), ff.getBarva());
            }
        } else {
            if (y2 < y1) {
                // 4. interpolace podle Y
                int yStart = Math.max(0, (int) v1.getY() + 1);
                double yEnd = Math.min(imageBuffer.getHeight() - 1, v2.getY());

                for (int y = yStart; y <= yEnd; y++) {
                    double t1 = (y - v1.getY()) / (v2.getY() - v1.getY());
                    Vrchol dd = v1.mul(1 - t1).add(v2.mul(t1));
                    nakresliPixel((int) Math.round(dd.getX()), (int) Math.round(dd.getY()), dd.getZ(), dd.getBarva());
                }
            }
        }
    }

    private void naplnUsecku(Vrchol a, Vrchol b) {
        if (a.getX() > b.getX()) {
            var docasne = a;
            a = b;
            b = docasne;
        }

        int xStart = Math.max(0, (int) a.getX() + 1);
        double xEnd = Math.min(imageBuffer.getWidth() - 1, b.getX());

        for (int x = xStart; x <= xEnd; x++) {
            double t = (x - a.getX()) / (b.getX() - a.getX());
            Vrchol ab = a.mul(1 - t).add(b.mul(t));

            nakresliPixel(x, (int) Math.round(ab.getY()), ab.getZ(), ab.getBarva());
        }
    }

    private void pripravBod(Vrchol v1) {
        Vrchol a = new Vrchol(
                v1.getBod().mul(model).mul(pohled).mul(projekce),
                v1.getBarva()
        );

        if (a.getX() > a.getW()) return; // bod je moc vpravo
        if (a.getX() < -a.getW()) return; // moc vlevo

        if (a.getY() > a.getW()) return; // moc nahoře
        if (a.getY() < -a.getW()) return; // moc dole

        if (a.getZ() > a.getW()) return; // moc vzadu/daleko (?)
        if (a.getZ() < 0) {
            // je za námi
        } else {
            // vidíme celou úsečku
            nakresliPixel((int) a.getX(), (int) a.getY(), a.getZ(), a.getBarva());
        }
    }

    private void nakresliPixel(int x, int y, double z, Col barva) {
        Optional<Double> zOptional = depthBuffer.getElement(x, y);
        if (zOptional.isPresent() && zOptional.get() > z) {
            depthBuffer.setElement(x, y, z);
            imageBuffer.setElement(x, y, barva.getRGB());
        }
    }

    private Vec3D transformujDoOkna(Vrchol vrchol) {
        // přednáška PGI_F, slide 90
        return new Vec3D(vrchol.getBod())
                .mul(new Vec3D(1, -1, 1)) // Y jde nahoru a my chceme, aby šlo dolů
                .add(new Vec3D(1, 1, 0)) // (0, 0) je uprostřed a my chceme, aby bylo vlevo nahoře
                .mul(new Vec3D(imageBuffer.getWidth() / 2.0, imageBuffer.getHeight() / 2.0, 1));
        // máme <0;2> -> vynásobíme polovinou velikosti plátna
    }

    private Vrchol vytvorVrcholZVektoruABarvy(Vec3D vektor3D, Col barva) {
        return new Vrchol(new Point3D(vektor3D), barva);
    }

    @Override
    public void procisti() {
        imageBuffer.clear();
        depthBuffer.clear();
    }

    @Override
    public void setModel(Mat4 model) {
        this.model = model;
    }

    @Override
    public void setPohled(Mat4 pohled) {
        this.pohled = pohled;
    }

    @Override
    public void setProjekce(Mat4 projekce) {
        this.projekce = projekce;
    }
}