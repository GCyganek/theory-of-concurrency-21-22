package pl.edu.agh.macwozni.dmeshparallel.myProductions;

import pl.edu.agh.macwozni.dmeshparallel.mesh.Vertex;
import pl.edu.agh.macwozni.dmeshparallel.production.AbstractProduction;

public class PS extends AbstractProduction<Vertex> {
    public PS(Vertex vertex) {
        super(vertex);
    }

    @Override
    public Vertex apply(Vertex vertex) {
        System.out.println("PS");
        Vertex southV = new Vertex(null, vertex, null, null, "M");
        vertex.setSouth(southV);
        return southV;
    }
}
