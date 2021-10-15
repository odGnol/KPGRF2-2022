package model3d;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    private final List<Solid> solids = new ArrayList<>();

    public List<Solid> getSolids() {
        return solids;
    }
}
