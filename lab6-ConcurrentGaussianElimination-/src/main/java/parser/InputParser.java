package parser;

import matrix.DoubleMatrixCell;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InputParser {

    public InputParser() {
    }

    public DoubleMatrixCell[][] parseInput(String inputFileName) throws IOException {
        BufferedReader reader = getFileBufferReader(inputFileName);
        // get N from file
        int N = Integer.parseInt(reader.readLine());

        // initialize augmented matrix
        DoubleMatrixCell[][] matrix = new DoubleMatrixCell[N][N + 1];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N + 1; j++) {
                matrix[i][j] = new DoubleMatrixCell();
            }
        }

        int rowIndex = 0;

        // fill augmented matrix
        for (String line : reader.lines().toList()) {
            String[] row = line.split(" ");
            if (rowIndex == N) {
                for (int i = 0; i < N; i++) {
                    matrix[i][N].setValue(Double.parseDouble(row[i]));
                }
            } else {
                for (int i = 0; i < N; i++) {
                    matrix[rowIndex][i].setValue(Double.parseDouble(row[i]));
                }
                rowIndex++;
            }
        }
        return matrix;
    }

    private BufferedReader getFileBufferReader(String inputFileName) throws IOException {
        Path path = Paths.get("src/main/resources/" + inputFileName);
        return Files.newBufferedReader(path);
    }
}
