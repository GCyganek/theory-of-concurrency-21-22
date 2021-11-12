package pl.edu.agh.macwozni.dmeshparallel.mesh;

import pl.edu.agh.macwozni.dmeshparallel.production.PDrawer;

import java.util.HashMap;
import java.util.Map;

public class GraphDrawer implements PDrawer<Vertex> {

    private final int N;

    public GraphDrawer(int N) {
        this.N = N;
    }

    @Override
    public void draw(Vertex vertex) {
        Map<Integer, Vertex> meshMap = new HashMap<>();
        fillMeshMap(vertex, meshMap);

        int firstColumnWithVertex = 1;
        while (!meshMap.containsKey(firstColumnWithVertex)) {
            firstColumnWithVertex++;
        }

        boolean keepLooping = true;
        StringBuilder secondLine = new StringBuilder();
        Vertex currentVertex;
        while (keepLooping) {
            keepLooping = false;
            for (int index = firstColumnWithVertex; index <= N; index++) {
                currentVertex = meshMap.get(index);

                if (currentVertex == null) {
                    System.out.print("    ");
                    secondLine.append("    ");
                }

                else {
                    keepLooping = true;
                    if (currentVertex.getEast() != null) {
                        System.out.print(currentVertex.label + "---");
                    }

                    else {
                        System.out.print(currentVertex.label + "   ");
                    }

                    if (currentVertex.getSouth() != null) {
                        secondLine.append("|   ");
                    }

                    else {
                        secondLine.append("    ");
                    }

                    meshMap.put(index, currentVertex.getSouth());
                }
            }
            System.out.println("\n" + secondLine);
            secondLine.setLength(0);
        }
    }

    private void fillMeshMap(Vertex vertex, Map<Integer, Vertex> meshMap) {
        while (vertex.north != null) {
            vertex = vertex.north;
        }

        while (vertex.east != null) {
            vertex = vertex.east;
        }

        int meshMapIndex = N;

        while (vertex.west != null) {
            meshMap.put(meshMapIndex, vertex);
            meshMapIndex--;
            vertex = vertex.west;
        }

        meshMap.put(meshMapIndex, vertex);
    }
}
