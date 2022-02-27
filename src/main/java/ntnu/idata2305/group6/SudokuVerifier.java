package ntnu.idata2305.group6;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SudokuVerifier {

    private static final int numberOfThreads = 3;
    private static int[][] sudoku = new int[9][9];

    private static boolean rowsVerified;
    private static boolean columnsVerified;
    private static boolean squaresVerified;


    public static class SudokuObject {
        int row;
        int column;
        public SudokuObject(int row, int column) {
            this.row = row;
            this.column = column;


        }
    }

    public static class SudokuRowsVerifier implements Runnable {

        @Override
        public void run() {

            int i;
            int x;
            for (x = 0; x < 9; x++) {
                boolean[] validityArray = new boolean[9];
                for (i = 0; i < 9; i++) {
                    int num = sudoku[x][i];
                    if (num < 1 || num > 9 || validityArray[num - 1]) {
                        rowsVerified = false;
                        return;
                    } else if (!validityArray[num - 1]) {
                        validityArray[num - 1] = true;
                    }
                }
            }

            rowsVerified = true;
        }
    }

    public static class SudokuColumnsVerifier implements Runnable {

        @Override
        public void run() {

            int i;
            int x;
            for (x = 0; x < 9; x++) {
                boolean[] validityArray = new boolean[9];
                for (i = 0; i < 9; i++) {
                    int num = sudoku[i][x];
                    if (num < 1 || num > 9 || validityArray[num - 1]) {
                        columnsVerified = false;
                        return;
                    } else if (!validityArray[num - 1]) {
                        validityArray[num - 1] = true;
                    }
                }
            }

            columnsVerified = true;
        }
    }

    public static class SudokuSquaresVerifier implements Runnable {

        @Override
        public void run() {

            int row;
            int column;
            int topRow;
            int leftColumn;
            for (topRow = 0; topRow < 9; topRow += 3) {
                for (leftColumn = 0; leftColumn < 9; leftColumn += 3) {
                    boolean[] validityArray = new boolean[9];
                    for (row = topRow; row < topRow + 3; row++) {
                        for (column = leftColumn; column < leftColumn + 3; column++) {
                            int num = sudoku[row][column];
                            if (num < 1 || num > 9 || validityArray[num - 1]) {
                                squaresVerified = false;
                                return;
                            } else {
                                validityArray[num - 1] = true;
                            }
                        }
                    }
                }
            }
            squaresVerified = true;

        }
    }


    public static void main(String[] args) {
        String fileName = "solvedsudoku.csv";

        Thread[] threads = new Thread[numberOfThreads];

        Thread CSVThread = new Thread(new CSVHandler(fileName));
        CSVThread.start();
        try {
            CSVThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threads[0] = new Thread(new SudokuRowsVerifier());
        threads[1] = new Thread(new SudokuColumnsVerifier());
        threads[2] = new Thread(new SudokuSquaresVerifier());





        for (int i = 0; i < threads.length; i++) {
                threads[i].start();
        }

        for (int i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (rowsVerified && columnsVerified && squaresVerified) {
            System.out.println("Sudoku is valid!");
        } else {
            System.out.println("Sudoku is invalid!");
        }
    }

    public static class CSVHandler implements Runnable {

        private String fileName;

        public CSVHandler(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public void run() {
            int i;
            int x = 0;
            String lineOfText;
            try (
                    BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass()
                            .getResourceAsStream("/" + fileName), StandardCharsets.UTF_8))
            ) {
                String[] separatedText;
                while ((lineOfText = reader.readLine()) != null) {
                    separatedText = lineOfText.split(",");
                    for (i = 0; i < separatedText.length; i++) {
                        sudoku[i][x] = Integer.parseInt(separatedText[i*x]);
                    }
                    x++;
                }

            } catch (IOException ioException) {
                System.err.println("asvljkg");

            } catch (IllegalArgumentException iae) {
                System.err.println("dsbjklv");

            } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                System.err.println("lzdfgjk");

            }
        }
    }
}
