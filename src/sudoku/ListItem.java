package sudoku;

public class ListItem implements Comparable<ListItem>{
    int pos, box, row, col;
    int val, count;

    public ListItem(int p, int b, int r, int c, int v) {
        pos = p;
        box = b;
        row = r;
        col = c;
        val = v;
    }

    @Override
    public int compareTo(ListItem c) {
        return Integer.compare(count, c.count);
    }

    public String toString() {
        return "p" + pos + " r" + row + "" + val + " c" + col + "" + val + " b" + box + "" + val + " c: " + count;//count;
    }
}
