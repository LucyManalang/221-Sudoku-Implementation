package sudoku;

import java.util.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GreedyTest {
    private GreedySolver greedySolver;
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

    public GreedyTest() {
       greedySolver = new GreedySolver(SIZE, BOARD, 0);
    }

    @Test
    public void TestFillList() {
       List<ListItem> candidateList = greedySolver.getCandidateList();
       assertEquals(candidateList, null);

       greedySolver.fillList();
       candidateList = greedySolver.getCandidateList();
       assertEquals(candidateList.size(), 141);
    }

    @Test
    public void TestFindKeyStones() {
        greedySolver.fillList();
        List<ListItem> candidateList = greedySolver.getCandidateList();
        List<ListItem> keyStones = greedySolver.findKeyStones();
        
        for (ListItem listItem : candidateList) {
            if (listItem.count == 1) {
                assertTrue(keyStones.contains(listItem));
            } else {
                assertFalse(keyStones.contains(listItem));
            }   
        }
    }

    @Test
    public void TestFillKeyStones() {
        greedySolver.fillList();
        List<ListItem> keyStones = new ArrayList<>();
        assertFalse(greedySolver.fillKeyStones(keyStones));

        keyStones = greedySolver.findKeyStones();
        assertTrue(greedySolver.fillKeyStones(keyStones));

        for (ListItem listItem : keyStones) {
            int row = listItem.row;
            int column = listItem.col;
            int value = listItem.val;

            assertEquals(greedySolver.getBoard()[row][column], value);
        }
    }

    @Test
    public void TestSolve() {
        greedySolver = new GreedySolver(SIZE, BOARD, 0);
        assertFalse(greedySolver.solved());
        
        greedySolver.solve();
        assertTrue(greedySolver.solved());
    }
}
