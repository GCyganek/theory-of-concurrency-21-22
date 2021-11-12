package pl.edu.agh.macwozni.dmeshparallel;

import pl.edu.agh.macwozni.dmeshparallel.mesh.Vertex;
import pl.edu.agh.macwozni.dmeshparallel.mesh.GraphDrawer;
import pl.edu.agh.macwozni.dmeshparallel.myProductions.PA;
import pl.edu.agh.macwozni.dmeshparallel.myProductions.PI;
import pl.edu.agh.macwozni.dmeshparallel.myProductions.PS;
import pl.edu.agh.macwozni.dmeshparallel.myProductions.PW;
import pl.edu.agh.macwozni.dmeshparallel.parallelism.BlockRunner;
import pl.edu.agh.macwozni.dmeshparallel.production.IProduction;
import pl.edu.agh.macwozni.dmeshparallel.production.PDrawer;

import java.util.HashMap;
import java.util.Map;

public class Executor extends Thread {

    private final int N;
    private final BlockRunner runner;
    
    public Executor(BlockRunner runner, int N){
        this.runner = runner;
        this.N = N;
    }

    @Override
    public void run() {
        // meshMap keeps recently added production (excluding PA productions) in each column
        Map<Integer, IProduction<Vertex>> meshMap = new HashMap<>();
        PDrawer<Vertex> drawer = new GraphDrawer(N);

        Vertex start = new Vertex(null, null, null, null, "S");
        PI pi = new PI(start);
        this.runner.addThread(pi);
        this.runner.startAll();
        drawer.draw(start);

        meshMap.put(N, pi);

        // creating upper right part of the mesh
        for (int index = N; index >= 2; index--) {
            // adding vertex to the west side
            PW pw = new PW(meshMap.get(index).getObj());
            this.runner.addThread(pw);
            meshMap.put(index - 1, pw);

            for (int column = index; column <= N; column++) {
                Vertex vertex = meshMap.get(column).getObj();

                if (column != N && column != index) {
                    // connecting current vertex with the one on the east side
                    this.runner.addThread(new PA(vertex));
                }
                // adding vertex to the south side of the current vertex
                PS ps = new PS(vertex);
                this.runner.addThread(ps);
                meshMap.put(column, ps);
            }

            this.runner.startAll();
            drawer.draw(start);
        }

        // completing the lower left part of the mesh
        for (int index = 0; index <= N - 1; index++) {
            for (int column = N - index; column >= 1; column--) {
                if (column == N) continue;

                // connecting current vertex with the one on the east side
                Vertex vertex = meshMap.get(column).getObj();
                // 'if' will prevent connecting the vertex in first row and first column with its east vertex
                if (vertex.getNorth() != null) {
                    this.runner.addThread(new PA(vertex));
                }

                if (column != N - index) {
                    // adding vertex to the south side of the current vertex
                    PS ps = new PS(vertex);
                    this.runner.addThread(ps);
                    meshMap.put(column, ps);
                }
            }

            this.runner.startAll();
            drawer.draw(start);
        }

        System.out.println("done");
    }
}
