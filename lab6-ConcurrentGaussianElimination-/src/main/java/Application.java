import matrix.DoubleMatrixCell;
import parallelism.ConcurrentBlockRunner;
import parser.InputParser;

import java.io.IOException;
/*
    Put the input file into the src/main/resources directory. Run the application with the name of the input file
    provided. Output file named "solved_output.txt" will be created in the main project directory
 */
public class Application {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Invalid program arguments number");
            System.exit(1);
        }

        InputParser inputParser = new InputParser();

        DoubleMatrixCell[][] augmentedMatrix;
        int N;

        try {
            augmentedMatrix = inputParser.parseInput(args[0]);
            N = augmentedMatrix.length;
            Executor executor = new Executor(new ConcurrentBlockRunner(), augmentedMatrix, N);
            executor.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
