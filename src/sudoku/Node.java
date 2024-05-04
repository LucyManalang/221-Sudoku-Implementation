package sudoku;

public class Node {
    Node left, right, up, down;
    int value;
    boolean sentinel; //used in the structure of the matrix
    int pos, box, row, col;


    public Node() {
        pos = box = row = col = -1;
        left = right = up = down = this;
    }

    public Node getColumn() {
        Node currNode = this;
        while (!currNode.sentinel) {
            currNode = currNode.up;
        }
        return currNode;
    }
    
    public Node getRow() {
        Node currNode = this;
        while (!currNode.sentinel) {
            currNode = currNode.left;
        }
        return currNode;
    }
}
