package pl.edu.agh.macwozni.dmeshparallel.mesh;

public class Vertex {

    //label
    String label;
    //links to adjacent elements
    Vertex east;
    Vertex north;
    Vertex west;
    Vertex south;

    //methods for adding links
    public Vertex(Vertex east, Vertex north, Vertex west, Vertex south, String label) {
        this.east = east;
        this.north = north;
        this.west = west;
        this.south = south;
        this.label = label;
    }
    //empty constructor

    public Vertex() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Vertex getEast() {
        return east;
    }

    public void setEast(Vertex east) {
        this.east = east;
    }

    public Vertex getNorth() {
        return north;
    }

    public void setNorth(Vertex north) {
        this.north = north;
    }

    public Vertex getWest() {
        return west;
    }

    public void setWest(Vertex west) {
        this.west = west;
    }

    public Vertex getSouth() {
        return south;
    }

    public void setSouth(Vertex south) {
        this.south = south;
    }
}
