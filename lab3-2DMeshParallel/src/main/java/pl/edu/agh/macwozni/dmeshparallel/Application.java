package pl.edu.agh.macwozni.dmeshparallel;

import pl.edu.agh.macwozni.dmeshparallel.parallelism.ConcurentBlockRunner;

class Application {

    public static void main(String args[]) {
        int N = 3;
        if (args.length > 0) {
            N = Integer.parseInt(args[0]);
        }

        Executor e = new Executor(new ConcurentBlockRunner(), N);
        e.start();
    }
}
