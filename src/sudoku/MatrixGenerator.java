package sudoku;

import java.util.*;
import java.util.Map.Entry;

public class MatrixGenerator {
    private int range, size;
    private Node start;
    private Map<Integer, Set<Integer>> positionMap, boxMap, rowMap, columnMap;

    /**
     * Generates the matrix for our Exact Cover Solver.
     * @param setup Pre-setup board
     * @param size size of board
     */
    public MatrixGenerator(BoardSetup setup, int size) {
        this.size = size;
        range = size * size;
        start = new Node();
        positionMap = setup.getPositionMap();
        boxMap = setup.getBoxMap();
        rowMap = setup.getRowMap();
        columnMap = setup.getColumnMap();
        structureMatrix();
        fillMatrix();
    }

    /**
     * Fills the matrix with all interior nodes
     */
    private void fillMatrix() {
        for (Node xNode = start.down; xNode != start; xNode = xNode.down) {  
            for (Node yNode = start.right; yNode != start; yNode = yNode.right) {
                if (xNode.pos == yNode.pos) placeNode(xNode, yNode);
                else if (xNode.value != yNode.value) continue;
                else if (xNode.box == yNode.box) placeNode(xNode, yNode);
                else if (xNode.row == yNode.row) placeNode(xNode, yNode);
                else if (xNode.col == yNode.col) placeNode(xNode, yNode);
            }
        }
    }

    /**
     * Places a node in matrix based on X and Y node
     * @param xNode
     * @param yNode
     */
    private void placeNode(Node xNode, Node yNode) {
        Node newNode = new Node();
        Node rightNode = xNode.left; // farthest right node
        Node downNode = yNode.up; // farthest down node
        newNode.value = 1;
        
        rightNode.right = newNode;
        xNode.left = newNode;
        newNode.left = rightNode;
        newNode.right = xNode;
        
        yNode.up.down = newNode;
        yNode.up = newNode;
        newNode.up = downNode;
        newNode.down = yNode;
    }

    /**
     * Finds the column with the fewest nodes for optimization.
     * @return best column
     */
    public Node findLowestColumn() {
        Node bestColumn = start;
        int bestCount = Integer.MAX_VALUE;
        int count;

        for (Node column = start.right; column != start; column = column.right) { 
            count = 0;
            for (Node row = column.down; row != column; row = row.down) { 
                count++;
            }
            if (count < bestCount) {
                bestCount = count;
                bestColumn = column;
            }
        }
        return bestColumn;
    }

    /**
     * Creates the structure of the matrix to be filled in later.
     */
    private void structureMatrix() {
        Node prevNode = start;
        start.sentinel = true;

        for (Entry<Integer, Set<Integer>> entry : positionMap.entrySet()) {
            int position = entry.getKey();
            for (int value : entry.getValue()) {
                Node currNode = new Node();

                currNode.sentinel = true;
                currNode.pos = position;
                currNode.row = position / range;
                currNode.col = position % range;
                currNode.box = ((currNode.row / size) * size) + (currNode.col / size);
                currNode.value = value;
                prevNode.down = currNode;
                currNode.up = prevNode;
                prevNode = currNode;
            }
        }

        start.up = prevNode;
        prevNode.down = start;
        prevNode = start;

        for (Entry<Integer, Set<Integer>> entry : positionMap.entrySet()) {
            int position = entry.getKey();
            Node currNode = new Node();

            currNode.sentinel = true;
            currNode.pos = position;
            prevNode.right = currNode;
            currNode.left = prevNode;
            prevNode = currNode;
        }

        for (Entry<Integer, Set<Integer>> entry : rowMap.entrySet()) {
            int row = entry.getKey();
            for (int value : entry.getValue()) {
                Node currNode = new Node();

                currNode.sentinel = true;
                currNode.row = row;
                currNode.value = value;
                prevNode.right = currNode;
                currNode.left = prevNode;
                prevNode = currNode;
            }
        }

        for (Entry<Integer, Set<Integer>> entry : columnMap.entrySet()) {
            int column = entry.getKey();
            for (int value : entry.getValue()) {
                Node currNode = new Node();

                currNode.sentinel = true;
                currNode.col = column;
                currNode.value = value;
                prevNode.right = currNode;
                currNode.left = prevNode;
                prevNode = currNode;
            }
        }

        for (Entry<Integer, Set<Integer>> entry : boxMap.entrySet()) {
            int box = entry.getKey();
            for (int value : entry.getValue()) {
                Node currNode = new Node();

                currNode.sentinel = true;
                currNode.box = box;
                currNode.value = value;
                prevNode.right = currNode;
                currNode.left = prevNode;
                prevNode = currNode;
            }
        }

        start.left = prevNode;
        prevNode.right = start;
    }

    public Node getStart() {
        return start;
    }

    /**
     * toString method mainly used for visualizing the matrix.
     */
    public String toString() {
        String str = "";
        for (Node xNode = start.down; xNode != start; xNode = xNode.down) {  
            for (Node yNode = start.right; yNode != start; yNode = yNode.right) {
                if (xNode.pos == yNode.pos) str += "1 ";
                else if (xNode.value != yNode.value) str += "0 ";
                else if (xNode.box == yNode.box) str += "1 ";
                else if (xNode.row == yNode.row) str += "1 ";
                else if (xNode.col == yNode.col) str += "1 ";
                else str += "0 ";
            }
            str += "\n";
        }
        return str;
    }

    public static void main(String[] args) {
        int size = 2;
        int[][] board = {
            {1, 3,  0, 0,},
            {4, 0,  3, 1,},

            {0, 0,  0, 3,},
            {3, 1,  0, 0,}
        };
        BoardSetup setup = new BoardSetup(size, board);
        System.out.println(new MatrixGenerator(setup, size).toString());
    }
}
