package ntnu.idata2305.group6;

public class SudokuVerifier {

    private static final int numberOfThreads = 3;
    private static final int[][] sudoku = {
            {6, 2, 4, 5, 3, 9, 1, 8, 7},
            {5, 1, 9, 7, 2, 8, 6, 3, 4},
            {8, 3, 7, 6, 1, 4, 2, 9, 5},
            {1, 4, 3, 8, 6, 5, 7, 2, 9},
            {9, 5, 8, 2, 4, 7, 3, 6, 1},
            {7, 6, 2, 3, 9, 1, 4, 5, 8},
            {3, 7, 1, 9, 5, 6, 8, 4, 2},
            {4, 9, 6, 1, 8, 2, 5, 7, 3},
            {2, 8, 5, 4, 7, 3, 9, 1, 6}
    };

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
        Thread[] threads = new Thread[numberOfThreads];
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
}
