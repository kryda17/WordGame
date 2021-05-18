package wordgame;

import java.util.*;

public class Table {

    private static final int GRID_SIZE = 15;
    private static final String BLACK_GRID_PLACEHOLDER = "####";
    private static final int MIN_BLACK_SQUARES = GRID_SIZE * GRID_SIZE / 10;
    //private static final int RANDOM_PLUS_BLACK_SQUARES = 10;
    private static final Random RND = new Random(1);

    private List<Coordinate> coordinates = new ArrayList<>();
    private String[][] table;

    public Table() {
        table = new String[GRID_SIZE][GRID_SIZE];
        makeBlackSquares();
        searchAndFillEmpytLines();
    }

    private void makeBlackSquares() {
        int numberOfBlackSquares = MIN_BLACK_SQUARES + RND.nextInt(MIN_BLACK_SQUARES);
        for (int i = 0; i < numberOfBlackSquares; i++) {
            Coordinate coord = generateCoordinate();
            insertBlackSquare(coord);
        }
    }

    private void insertBlackSquare(Coordinate coord) {
        table[coord.getxCoord()][coord.getyCoord()] = BLACK_GRID_PLACEHOLDER;
    }


    private Coordinate generateCoordinate() {
        int xCoord = RND.nextInt(GRID_SIZE);
        int yCoord = RND.nextInt(GRID_SIZE);
        Coordinate coord = new Coordinate(xCoord, yCoord);

        if (isGeneratedCoordPosGood(coord)) {
            return coord;
        }
        return generateCoordinate();
    }

    private boolean isGeneratedCoordPosGood(Coordinate coordinate) {
        int x = coordinate.getxCoord();
        int y = coordinate.getyCoord();

        /*if (x == 1 || x == GRID_SIZE - 2 || y == 1 || y == GRID_SIZE - 2) {
            return false;
        }
         */

        for (Coordinate secCoord : coordinates) {

            int xDiff = Math.abs(x - secCoord.getxCoord());
            int yDiff = Math.abs(y - secCoord.getyCoord());

            if ((xDiff == 0 && yDiff < 3) || (yDiff == 0 && xDiff < 3)) {
                return false;
            }
        }

        coordinates.add(coordinate);
        return true;
    }

    private void searchAndFillEmpytLines() {
        List<Integer> emptyRow = findEmptyRows();
        for (int i : emptyRow) {
            Coordinate coord = generateYCoordinate(i);
            insertBlackSquare(coord);
        }

        List<Integer> emptyColumn = findEmptyColumns();
        for (int i : emptyColumn) {
            Coordinate coord = generateXCoordinate(i);
            insertBlackSquare(coord);
        }
    }

    private Coordinate generateYCoordinate(int xCoord) {
        int yCoord = RND.nextInt(GRID_SIZE);
        Coordinate coord = new Coordinate(xCoord, yCoord);

        if (isGeneratedCoordPosGood(coord)) {
            return coord;
        }
        return generateYCoordinate(xCoord);
    }

    private Coordinate generateXCoordinate(int yCoord) {
        int xCoord = RND.nextInt(GRID_SIZE);
        Coordinate coord = new Coordinate(xCoord, yCoord);

        if (isGeneratedCoordPosGood(coord)) {
            return coord;
        }
        return generateXCoordinate(yCoord);
    }

    private List<Integer> findEmptyRows() {
        List<Integer> emptyRow = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (table[i][j] != null) {
                    break;
                }
                if (j == GRID_SIZE - 1) {
                    emptyRow.add(i);
                }
            }
        }
        return emptyRow;
    }

    private List<Integer> findEmptyColumns() {
        List<Integer> emptyColumns = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (table[j][i] != null) {
                    break;
                }
                if (j == GRID_SIZE - 1) {
                    emptyColumns.add(i);
                }
            }
        }
        return emptyColumns;
    }

    public void printTable() {
        for (String[] str : table) {
            for (String s : str) {
                System.out.print(s + " ");
            }
            System.out.println();
        }
    }

    /*private boolean isAllRowColContainsBlackSquare() {
        for (Coordinate item : coordinates) {
            int x = item.getxCoord();
            int y = item.getyCoord();
            if ()
        }
    }

     */

}
