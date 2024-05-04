package sudoku;

public class ExactCoverSolver {
    private MatrixGenerator matrix;
    private long delay;
    private int range, boxSize;
    private int[][] board;
    private Node start;

    /**
     * Exact cover solution for sudoku using algorithm X. Based off pseudocode from Johnathan Chu.
     * https://www.ocf.berkeley.edu/~jchu/publicportal/sudoku/sudoku.paper.html
     * @param size
     * @param board
     * @param delay
     */
    public ExactCoverSolver(int size, int[][] board, long delay) {
        boxSize = size;
        range = boxSize * boxSize;

        BoardSetup setup = new BoardSetup(size, board);
        matrix = new MatrixGenerator(setup, size);

        this.board = board;
        this.delay = delay;

        start = matrix.getStart();
    }

    /**
     * Calls search method
     */
    public void solve() {
        search(matrix.findLowestColumn());
    }

    /**
     * Recursive Algorithm X call
     * @param columnNode Column being checked
     * @return true if matrix is empty, false otherwise.
     */
    public boolean search(Node columnNode) {
        if (delay > 0) { // if you want to watch the algorithm step through the board
            printBoard();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (start.right == start) {
            return true;
        }

        if (columnNode.down.sentinel)
            return search(matrix.findLowestColumn());

        for (Node node = columnNode.down; !node.sentinel; node = node.down) {
            Node rowNode = node.getRow();
            int row = rowNode.row;
            int column = rowNode.col;
            board[row][column] = rowNode.value;

            for (Node rightNode = rowNode.right; rightNode != rowNode; rightNode = rightNode.right) {
                cover(rightNode.getColumn());
            }

            if (search(matrix.findLowestColumn())) return true;

            board[row][column] = 0;
            for (Node leftNode = rowNode.left; leftNode != rowNode; leftNode = leftNode.left) {
                uncover(leftNode.getColumn());
            }
        }

        return false;
    }

    /**
     * Covers column
     * @param column column to be covered
     */
    public void cover(Node column) {
        column.right.left = column.left;
        column.left.right = column.right;

        for (Node row = column.down; !row.sentinel; row = row.down) {
            for (Node rightNode = row.getRow().right; !rightNode.sentinel; rightNode = rightNode.right) {
                rightNode.up.down = rightNode.down;
                rightNode.down.up = rightNode.up;
            }
        }
    }
    
    /**
     * Uncovers previously covered column
     * @param column column to be uncovered
     */
    public void uncover(Node column) {
        for (Node row = column.up; row != column; row = row.up) {
            for (Node leftNode = row.left; leftNode != row; leftNode = leftNode.right) {
                leftNode.up.down = leftNode.down;
                leftNode.down.up = leftNode.up;
            }
            column.right.left = column.left;
            column.left.right = column.right;
        }
    }

    /**
     * Using set sizes, checks if the board has been solved. If it has not, it is
     * not solvable.
     */
    public boolean solved() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0)
                    return false;
            }
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
            if (i % boxSize == 0)
                str += "\n";
            for (int j = 0; j < range; j++) {
                if (j % boxSize == 0)
                    str += "  ";
                str += board[i][j] + " ";
            }
        }
        str += "\n\n";
        System.out.println(str);
    }

    public static void main(String[] args) {
        int size = 3;
        long delay = 30;
        int[][] board = {
        { 0, 0, 0, 6, 4, 7, 0, 9, 1 },
        { 6, 0, 0, 0, 0, 0, 0, 0, 2 },
        { 0, 0, 1, 0, 0, 8, 7, 0, 0 },

        { 7, 0, 9, 0, 0, 0, 3, 5, 0 },
        { 1, 0, 0, 0, 2, 0, 0, 0, 0 },
        { 0, 0, 0, 4, 0, 0, 0, 1, 7 },

        { 0, 0, 0, 0, 0, 0, 0, 2, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 9, 5, 0, 0, 3, 0, 4, 0 }
        };

        long ms = new ExactCoverSolver(size, board, delay).timer();

        System.out.println(
                "A " + size * size + "x" + size * size + " puzzle took "
                        + ms + "ms with a delay of " + delay + "ms.\n");
    }
}
