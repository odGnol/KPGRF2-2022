package renderer;

import model.Cast;
import model.Vrchol;
import transforms.Mat4;

import java.util.List;

public interface GPURenderer {

    void nakresli(List<Cast> parts, List<Integer> ib, List<Vrchol> vb);

    void procisti();

    void setModel(Mat4 model); // modelovací (kombinace scale, rotace, posunutí)

    void setView(Mat4 view); // pohledová

    void setProjection(Mat4 projection); // projekční

}
