package pl.edu.agh.macwozni.dmeshparallel.myProductions;

import pl.edu.agh.macwozni.dmeshparallel.mesh.Vertex;
import pl.edu.agh.macwozni.dmeshparallel.production.AbstractProduction;

public class PA extends AbstractProduction<Vertex> {
    public PA(Vertex vertex) {
        super(vertex);
    }

    @Override
    public Vertex apply(Vertex vertex) {
        System.out.println("PA");
        Vertex eastV = vertex.getNorth().getEast().getSouth();
        vertex.setEast(eastV);
        eastV.setWest(vertex);
        return vertex;
    }
}
