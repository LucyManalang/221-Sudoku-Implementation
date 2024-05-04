package sudoku;

import java.util.*;

public class BruteForceSolver{
    private long delay;
    private int range;
    private int boxSize;
    private int[][] board;
    private Map<Integer, Set<Integer>> boxMap, rowMap, columnMap;

    /**
     * Brute force solver for Sudoku, a Big-Oh analysis gives us (range)^(boardSize)
     * @param size The size of each box
     * @param seed Seed of the generated board
     * @param delay Delay (ms) if the user wants to watch the algorithm step through.
     */
    public BruteForceSolver (int size, int[][] board, long delay) {
        BoardSetup setup = new BoardSetup(size, board);
        this.delay = delay;
        this.board = board;
        boxSize = size;
        range = boxSize * boxSize;
        boxMap = setup.getBoxMap();
        rowMap = setup.getRowMap();
        columnMap = setup.getColumnMap();
    }
    
    /**
     * Calls the brute-force solver
     */
    public void solve() {
        bruteForce(board, 0, 0);
    }

    /**
     * Recursive method to solve sudoku
     */
    private boolean bruteForce(int[][] board, int row, int column) {
        if (delay > 0) { //if you want to watch the algorithm step through the board
            printBoard();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (row == range - 1 && column == range) {
            return true;
        }
        if (column == range) {
            row++;
            column = 0;
        }
        if (board[row][column] != 0) return bruteForce(board, row, column + 1);

        int box = ((row / boxSize) * boxSize) + (column / boxSize);
        for (int i = 1; i <= range; i++) {
            if (placeable(i, box, row, column)) {
                board[row][column] = i;
                boxMap.get(box).remove(i);
                rowMap.get(row).remove(i);
                columnMap.get(column).remove(i);

                if (bruteForce(board, row, column)) return true;
                boxMap.get(box).add(i);
                rowMap.get(row).add(i);
                columnMap.get(column).add(i);
            }

            board[row][column] = 0;
        }
        return false;
    }


    /**
     * Checks if a value is placable at a square
     */
    public boolean placeable(int value, int box, int row, int column) {
        if (!boxMap.get(box).contains(value)) return false;
        if (!rowMap.get(row).contains(value)) return false;
        if (!columnMap.get(column).contains(value)) return false;
        return true;
    }
  
    /**
     * Using set sizes, checks if the board has been solved. If it has not, it is not solvable.
     */
    public boolean solved() {
        for (int i = 0; i < range; i++) {
            if (boxMap.get(i).size() != 0) return false;
            if (rowMap.get(i).size() != 0) return false;
            if (columnMap.get(i).size() != 0) return false;
        }
        return true;
    }

    /**
     * Times the brute force algorithm
     */
    public long timer() {
        long startTime = System.currentTimeMillis();
        solve();
        long runTime = System.currentTimeMillis() - startTime;

        if (solved()) {
            printBoard();
            return runTime;
        } 
        System.out.println("Unsolvable");
        return -1;
    }

    /**
     * Prints the board to be visualized
     */
    public void printBoard() {
        String str = "";

        for (int i = 0; i < range; i++) {
            str += "\n";
            if (i % boxSize == 0) str += "\n";
            for (int j = 0; j < range; j++) {
                if (j % boxSize == 0) str += "  ";
                str += board[i][j] + " ";
            }
        }
        str += "\n\n";
        System.out.println(str);
    }

    public static void main(String[] args) {
        int size = 3;
        long delay = 1;
        int[][] board = {
            {0, 0, 0,  6, 4, 7,  0, 9, 1},
            {6, 0, 0,  0, 0, 0,  0, 0, 2},
            {0, 0, 1,  0, 0, 8,  7, 0, 0},
    
            {7, 0, 9,  0, 0, 0,  3, 5, 0},
            {1, 0, 0,  0, 2, 0,  0, 0, 0},
            {0, 0, 0,  4, 0, 0,  0, 1, 7},
    
            {0, 0, 0,  0, 0, 0,  0, 2, 0},
            {0, 0, 0,  0, 0, 0,  0, 0, 0},
            {0, 9, 5,  0, 0, 3,  0, 4, 0}
        };
        
        long ms = new BruteForceSolver(size, board, delay).timer();
         
        System.out.println(
            "A " + size*size + "x" + size*size + " puzzle took "
             + ms + "ms with a delay of " + delay + "ms.\n"
            );
    }
}


// :3 meow meow meow


