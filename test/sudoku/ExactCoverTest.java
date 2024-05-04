package sudoku;

import java.util.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExactCoverTest {
    private ExactCoverSolver exactCoverSolver;
    private MatrixGenerator matrix;
    private Node start;
    private final int SIZE = 3;
    private final int[][] BOARD = {
        { 3, 0, 6,  5, 0, 8,  4, 0, 0 },
        { 5, 2, 0,  0, 0, 0,  0, 0, 0 },
        { 0, 8, 7,  0, 0, 0,  0, 3, 1 },

        { 0, 0, 3,  0, 1, 0,  0, 8, 0 },
        { 9, 0, 0,  8, 6, 3,  0, 0, 5 },
        { 0, 5, 0,  0, 9, 0,  6, 0, 0 },

        { 1, 3, 0,  0, 0, 0,  2, 5, 0 },
        { 0, 0, 0,  0, 0, 0,  0, 7, 4 },
        { 0, 0, 5,  2, 0, 6,  3, 0, 0 } 
    };

    public ExactCoverTest() {
        BoardSetup setup = new BoardSetup(SIZE, BOARD);
        matrix = new MatrixGenerator(setup, SIZE);
        exactCoverSolver = new ExactCoverSolver(SIZE, BOARD, 0);
        start = matrix.getStart();
    }

    @Test
    public void TestCover() {
        int xSize = 0;
        int ySize = 0;
        for (Node x = start.right; x != start; x = x.right) {
            xSize++;
        }
        for (Node y = start.down; y != start; y = y.down) {
            ySize++;
        }
        
        assertEquals(xSize, 196);
        assertEquals(ySize, 141);
        
        exactCoverSolver.cover(start.right.right);

        xSize = 0;
        ySize = 0;
        for (Node x = start.right; x != start; x = x.right) {
            xSize++;
        }
        for (Node y = start.down; y != start; y = y.down) {
            ySize++;
        }

        assertEquals(xSize, 195);
        assertEquals(ySize, 141);
    }

    @Test
    public void TestSolve() {
        exactCoverSolver = new ExactCoverSolver(SIZE, BOARD, 0);
        assertFalse(exactCoverSolver.solved());
        
        exactCoverSolver.solve();
        assertTrue(exactCoverSolver.solved());
    }
}
