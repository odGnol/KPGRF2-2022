package renderer;

import model.Cast;
import model.Vrchol;
import transforms.Mat4;

import java.util.List;

public interface GPURenderer {

    void nakresli(List<Cast> casti, List<Integer> ib, List<Vrchol> vb);

    void procisti();

    void setModel(Mat4 model); // modelovací (kombinace scale, rotace, posunutí)

    void setPohled(Mat4 pohled); // pohledová

    void setProjekce(Mat4 projekce); // projekční

    void setDratovyModel(boolean jeDratovy);
}
