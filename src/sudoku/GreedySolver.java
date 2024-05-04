package sudoku;

import java.util.*;
import java.util.Map.Entry;

public class GreedySolver {
    private long delay;
    private boolean solved;
    private List<ListItem> candidateList, keyStones;
    private int range;
    private int boxSize;
    private int[][] board;
    private Map<Integer, Set<Integer>> positionMap, boxMap, rowMap, columnMap;

    public GreedySolver (int size, int[][] board, long delay) {
        BoardSetup setup = new BoardSetup(size, board);
        this.board = board;
        this.delay = delay;
        boxSize = size;
        range = boxSize * boxSize;
        positionMap = setup.getPositionMap();
        boxMap = setup.getBoxMap();
        rowMap = setup.getRowMap();
        columnMap = setup.getColumnMap();
    }


    public void solve() {
        solved = greedy();
    }

    public boolean greedy() {
        if (delay > 0) { //if you want to watch the algorithm step through the board
            printBoard();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        fillList();
        keyStones = findKeyStones();
        if (!fillKeyStones(keyStones)) {
            return true;
        }

        if (!solvable(keyStones)){
            return false;
        }
        return greedy();
    }

    public boolean fillKeyStones(List<ListItem> keyStones) {
        if (keyStones.isEmpty()) return false;

        for (ListItem li : keyStones) {
            int row = li.row;
            int column = li.col;
            int value = li.val;
            addPosition(li);

            board[row][column] = value;
        }
        return true;
    }

    public List<ListItem> findKeyStones() {
        List<ListItem> keyStones = new ArrayList<>();
        int pCount, bCount, rCount, cCount;
        for (ListItem i : candidateList) {
            pCount = 1;
            bCount = 1;
            rCount = 1;
            cCount = 1;
            for (ListItem j : candidateList) {
                if (i == j) continue;
                if (i.pos == j.pos) pCount++;
                if (i.val != j.val) continue;
                if (i.box == j.box) bCount++;
                if (i.row == j.row) rCount++;
                if (i.col == j.col) cCount++;
            }
            int count1 = Math.min(pCount, bCount);
            int count2 = Math.min(rCount, cCount);
            i.count = Math.min(count1, count2);
        }
        Collections.sort(candidateList);

        for (ListItem li : candidateList) {
            if (li.count == 1) {
                keyStones.add(li);
            } else {
                break;
            }
        }
        return keyStones;
    }

    private boolean solvable(List<ListItem> keystones) {
        for (ListItem li1 : keystones) {
            for (ListItem li2 : keystones) {
                if (li1 == li2) continue;
                if (li1.val != li2.val) continue;
                if (li1.pos == li2.pos) return false;
                if (li1.box == li2.box) return false;
                if (li1.row == li2.row) return false;
                if (li1.col == li2.col) return false; 
            }
        }
        return true;
    }

    private void addPosition(ListItem item) {
        int position = item.pos;
        int box = item.box;
        int row = item.row;
        int column = item.col;
        int value = item.val;

        board[row][column] = value;
        positionMap.remove(position);
        boxMap.get(box).remove(value);
        rowMap.get(row).remove(value);
        columnMap.get(column).remove(value);
        updatePosition(item);
        candidateList.remove(item);
    }

    public void fillList() {
        candidateList = new ArrayList<>();
        for (Entry<Integer, Set<Integer>> entry : positionMap.entrySet()) {
            int position = entry.getKey();
            int row = position / range;
            int column = position % range;
            int box = ((row / boxSize) * boxSize) + (column / boxSize);
            Set<Integer> positionSet = positionMap.get(position);

            for (Integer i : positionSet) {
                ListItem li = new ListItem(position, box, row, column, i);
                candidateList.add(li);
            } 
        }
    }

    private void updatePosition(ListItem item) {
        int row = item.row;
        int column = item.col;
        int value = item.val;
        
        for (int i = 0; i < range; i++) {
            if (board[row][i] != 0) continue;
            positionMap.get(range * row + i).remove(value);
        }

        for (int i = 0; i < range; i++) {
            if (board[i][column] != 0) continue;
            positionMap.get(range * i + column).remove(value);
        }

        int startRow = row - row % boxSize; 
        int startColumn = column - column % boxSize;
        for (int i = startRow; i < startRow + boxSize; i++) {
            for (int j = startColumn; j < startColumn + boxSize; j++) {
                if (board[i][j] != 0) continue;
                positionMap.get(range * i + j).remove(value);
            }
        }        
    }

    public boolean solved() {
        return solved;
    }

    public long timer() {
        long startTime = System.currentTimeMillis();
        solve();
        long runTime = System.currentTimeMillis() - startTime;

        if (solved()) {
            printBoard();
            return runTime;
        }
        System.out.println("Unsolvable using GreedySolver");
        return -1;
    }

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

    public List<ListItem> getCandidateList() {
        return candidateList;
    }

    public int[][] getBoard() {
        return board;
    }


    public static void main(String[] args) {
        int size = 3;
        long delay = 250;
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

        long ms = new GreedySolver(size, board, delay).timer();

        System.out.println(
            "A " + size*size + "x" + size*size + " puzzle took "
             + ms + "ms with a delay of " + delay + "ms.\n"
            );

    }
}

