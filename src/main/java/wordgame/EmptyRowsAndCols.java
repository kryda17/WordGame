package wordgame;

import java.util.List;

public class EmptyRowsAndCols {

    private List<Integer> rows;
    private List<Integer> cols;

    public EmptyRowsAndCols(List<Integer> rows, List<Integer> cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public List<Integer> getRows() {
        return List.copyOf(rows);
    }

    public List<Integer> getCols() {
        return  List.copyOf(cols);
    }
}
