package renderer;

import model3d.Scene;
import transforms.Mat4;

public interface GPURenderer {

    void draw(Scene scene);

    void setModel(Mat4 model); // modelovací (kombinace scale, rotace, posunutí)

    void setView(Mat4 view); // pohledová

    void setProjection(Mat4 projection); // projekční

}
