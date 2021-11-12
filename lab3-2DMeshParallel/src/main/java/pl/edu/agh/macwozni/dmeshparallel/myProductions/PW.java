package pl.edu.agh.macwozni.dmeshparallel.myProductions;

import pl.edu.agh.macwozni.dmeshparallel.mesh.Vertex;
import pl.edu.agh.macwozni.dmeshparallel.production.AbstractProduction;

public class PW extends AbstractProduction<Vertex> {
    public PW(Vertex vertex) {
        super(vertex);
    }

    @Override
    public Vertex apply(Vertex vertex) {
        System.out.println("PW");
        Vertex west = new Vertex(vertex, null, null, null, "M");
        vertex.setWest(west);
        return west;
    }
}
