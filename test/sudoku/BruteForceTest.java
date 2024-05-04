package sudoku;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BruteForceTest {
    private BruteForceSolver bfSolver;
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

    public BruteForceTest() {
        bfSolver = new BruteForceSolver(SIZE, BOARD, 0);
    }

    @Test
    public void TestPlacable() {
        int[] placableAt4_2 = {1, 2, 4};
        int[] notPlacableAt4_2 = {3, 5, 6, 7, 8, 9};
        int[] placableAt5_8 = {2, 3, 7};
        int[] notPlacableAt5_8 = {1, 4, 5, 6, 8, 9};

        for (int i : placableAt4_2) {
            assertTrue(bfSolver.placeable(i, 3, 4, 2));
        }
        for (int i : notPlacableAt4_2) {
            assertFalse(bfSolver.placeable(i, 3, 4, 2));
        }

        for (int i : placableAt5_8) {
            assertTrue(bfSolver.placeable(i, 5, 5, 8));
        }
        for (int i : notPlacableAt5_8) {
            assertFalse(bfSolver.placeable(i, 5, 5, 8));
        }
    }

        
    @Test
    public void TestSolve() {
        bfSolver.solve();
        assertTrue(bfSolver.solved());
    }
}
