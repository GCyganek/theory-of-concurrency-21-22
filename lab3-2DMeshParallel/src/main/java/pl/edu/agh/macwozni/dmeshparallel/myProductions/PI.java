package pl.edu.agh.macwozni.dmeshparallel.myProductions;

import pl.edu.agh.macwozni.dmeshparallel.mesh.Vertex;
import pl.edu.agh.macwozni.dmeshparallel.production.AbstractProduction;

public class PI extends AbstractProduction<Vertex> {
    public PI(Vertex vertex) {
        super(vertex);
    }

    @Override
    public Vertex apply(Vertex vertex) {
        System.out.println("PI");
        vertex.setLabel("M");
        return vertex;
    }
}
