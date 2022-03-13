package renderer;

import model.Part;
import model.Vertex;
import transforms.Mat4;

import java.util.List;

public interface GPURenderer {

    void draw(List<Part> parts, List<Integer> ib, List<Vertex> vb);

    void clear();

    void setModel(Mat4 model); // modelovací (kombinace scale, rotace, posunutí)

    void setView(Mat4 view); // pohledová

    void setProjection(Mat4 projection); // projekční

}
