package sudoku;

import java.util.*;
import java.util.Map.Entry;

/**
 * Generates a Sudoku Board
 */
public class BoardSetup {
    private int range;
    private int boxSize;
    private int[][] board;
    private Map<Integer, Set<Integer>> positionMap, boxMap, rowMap, columnMap;
    
    public BoardSetup(int inputSize, int[][] board) { 
        this.board = board;
        boxSize = inputSize;
        range = boxSize * boxSize;

        initalizeMaps();
        printBoard();
    }

    private void initalizeMaps() {
        boxMap = createMap();
        rowMap = createMap();
        columnMap = createMap();
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                int box = ((i / boxSize) * boxSize) + (j / boxSize);
                int value = board[i][j];

                if (value != 0) {
                    boxMap.get(box).add(value);
                    rowMap.get(i).add(value);
                    columnMap.get(j).add(value);
                } 
            }
        }
        createPositionMap();
        processPositions();
    }

    private void processPositions() {
        complimentMap(boxMap);
        complimentMap(rowMap);
        complimentMap(columnMap);

        for (Entry<Integer, Set<Integer>> entry : positionMap.entrySet()) {
            int position = entry.getKey();
            int row = position / range;
            int column = position % range;
            int box = ((row / boxSize) * boxSize) + (column / boxSize);
            Set<Integer> positionSet = entry.getValue();

            positionSet.retainAll(boxMap.get(box));
            positionSet.retainAll(rowMap.get(row));
            positionSet.retainAll(columnMap.get(column));
            
            positionMap.put(entry.getKey(), positionSet);
        }
    }

    private void createPositionMap() {
        positionMap = new HashMap<>();

        for (int i = 0; i < range; i++) {
            for (int j = 0; j < range; j++) {
                if (board[i][j] == 0) {
                    Set<Integer> positionSet = new HashSet<>();
                    for (int k = 1; k <= range; k++) {
                        positionSet.add(k);
                    }
                    int position = range * i + j;
                    positionMap.put(position, positionSet);
                }
            }
        }
    }

    private Map<Integer, Set<Integer>> createMap() {
        Map<Integer, Set<Integer>> map = new HashMap<>();

        for (int index = 0; index < range; index++) {
            Set<Integer> set = new HashSet<>();
            map.put(index, set);
        }
        return map;
    }

    private void complimentMap(Map<Integer, Set<Integer>> inputMap) {
        for (Set<Integer> set: inputMap.values()) {
            for (int i = 1; i <= range; i++) {
                if (set.contains(i)) set.remove(i);
                else set.add(i);
            }
        }
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

    public Map<Integer, Set<Integer>> getPositionMap() {
        return positionMap;
    }

    public Map<Integer, Set<Integer>> getBoxMap() {
        return boxMap;
    }

    public Map<Integer, Set<Integer>> getRowMap() {
        return rowMap;
    }

    public Map<Integer, Set<Integer>> getColumnMap() {
        return columnMap;
    }
}






