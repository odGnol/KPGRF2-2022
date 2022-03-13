package model;

// viz slide 16 - KPGR2/přednášky/PGII_01.pdf
public class Part {

    private final TopologyType topologyType;
    private final int index; // offset? start?
    private final int count;

    public Part(TopologyType topologyType, int index, int count) {
        this.topologyType = topologyType;
        this.index = index;
        this.count = count;
    }

    public TopologyType getTopologyType() {
        return topologyType;
    }

    public int getIndex() {
        return index;
    }

    public int getCount() {
        return count;
    }

}
