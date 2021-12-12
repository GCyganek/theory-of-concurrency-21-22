package output;

import matrix.DoubleMatrixCell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class OutputCreator {

    public static void createOutput(DoubleMatrixCell[][] augmentedMatrix, int N) throws FileNotFoundException {
        File outputFile = new File("solved_output.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        PrintStream printStream = new PrintStream(fileOutputStream);

        System.setOut(printStream);

        // printing matrix size
        System.out.println(N);

        // printing matrix
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                System.out.print(augmentedMatrix[row][col] + " ");
            }
            System.out.println();
        }

        // printing solution
        for (int row = 0; row < N; row++) {
            System.out.print(augmentedMatrix[row][N] + " ");
        }
        System.out.println();
    }
}
