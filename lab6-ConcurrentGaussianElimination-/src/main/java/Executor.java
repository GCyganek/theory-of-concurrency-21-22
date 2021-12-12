import matrix.DoubleMatrixCell;
import output.OutputCreator;
import parallelism.BlockRunner;
import production.myProductions.A;
import production.myProductions.B;
import production.myProductions.C;
import util.TwoIntegersKey;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Executor extends Thread {

    private final BlockRunner runner;
    private final DoubleMatrixCell[][] augmentedMatrix;
    private final int N;

    public Executor(BlockRunner runner, DoubleMatrixCell[][] augmentedMatrix, int N) {
        this.runner = runner;
        this.augmentedMatrix = augmentedMatrix;
        this.N = N;
    }

    @Override
    public void run() {
        Map<Integer, A> mMap = new HashMap<>();
        Map<TwoIntegersKey, B> nMap = new HashMap<>();

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                A a = new A(augmentedMatrix[j][i], augmentedMatrix[i][i]);
                this.runner.addThread(a);
                mMap.put(j, a);
            }

            this.runner.startAll();

            for (int row = i + 1; row < N; row++) {
                for (int col = i; col < N + 1; col++) {
                    B b = new B(augmentedMatrix[i][col], mMap.get(row).getObj());
                    this.runner.addThread(b);
                    nMap.put(new TwoIntegersKey(row, col), b);
                }
            }

            this.runner.startAll();

            for (int row = i + 1; row < N; row++) {
                for (int col = i; col < N + 1; col++) {
                    C c = new C(augmentedMatrix[row][col], nMap.get(new TwoIntegersKey(row, col)).getObj());
                    this.runner.addThread(c);
                }
            }

            this.runner.startAll();
        }

        // backward substitution
        for (int col = N - 1; col > -1; col--) {
            for (int row = col - 1; row > -1; row--) {
                double factor = augmentedMatrix[row][col].getValue() / augmentedMatrix[col][col].getValue();
                augmentedMatrix[row][col].subtract(factor * augmentedMatrix[col][col].getValue());
                augmentedMatrix[row][N].subtract(factor * augmentedMatrix[col][N].getValue());
            }

            augmentedMatrix[col][N].divide(augmentedMatrix[col][col].getValue());
            augmentedMatrix[col][col].divide(augmentedMatrix[col][col].getValue());
        }

        // creating output file
        try {
            OutputCreator.createOutput(augmentedMatrix, N);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
