package wordgame.chris.jpa;

import java.util.*;

public class Table2 {

    public final int GRID_SIZE;
    public final String EMPTY_GRID_PLACEHOLDER = "0";
    public final String BLACK_GRID_PLACEHOLDER = "#";
    private final int BLACKS_MIN_DISTANCE = 2;
    private final int MIN_BLACK_SQUARES;//Hogy minden sorban és oszlopban legyen egy,az egyenlő a GRID_SIZE
    private final int MIN_ADDITIONAL_BLACK_SQUARE;

    private String[][] table;
    private Random rnd;

    public Table2(int GRID_SIZE) {
        this.GRID_SIZE = GRID_SIZE;
        MIN_BLACK_SQUARES = GRID_SIZE;
        MIN_ADDITIONAL_BLACK_SQUARE = GRID_SIZE / 2;
        rnd = new Random();
        initTable();
    }

    public Table2(String[][] table) {
        this.table = table;
        GRID_SIZE = table.length;
        MIN_BLACK_SQUARES = GRID_SIZE;
        MIN_ADDITIONAL_BLACK_SQUARE = GRID_SIZE / 2;
    }

    public Table2(int GRID_SIZE, Random rnd) {
        this.GRID_SIZE = GRID_SIZE;
        table = new String[GRID_SIZE][GRID_SIZE];
        MIN_BLACK_SQUARES = GRID_SIZE;
        MIN_ADDITIONAL_BLACK_SQUARE = GRID_SIZE / 2;
        this.rnd = rnd;
        initTable();
    }

    private void initTable() {
        fillTalbeWithEmptys();
        makeBlackSquares();
    }

    private void fillTalbeWithEmptys() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                deleteCoordinate(i, j);
            }
        }
    }

    private void makeBlackSquares() {
        generateRndBlacks();
        generateAdditionalRandomBlacks();
    }



    private void generateAdditionalRandomBlacks() {
        int rnd_num_of_black_squares = rnd.nextInt(MIN_BLACK_SQUARES) + MIN_ADDITIONAL_BLACK_SQUARE;
        int allRequiredBlackSquare = MIN_BLACK_SQUARES + rnd_num_of_black_squares;
        for (int i = 0; i < allRequiredBlackSquare; i++) {
            genarateBlack();
        }
    }

    private void genarateBlack() {
        int x = rnd.nextInt(GRID_SIZE);
        int y = rnd.nextInt(GRID_SIZE);
        Coordinate coordinate = new Coordinate(x,y);
        if (isGeneratedCoordDifferenceMinTwoV3(coordinate)) {
            insertBlack(coordinate);
        }
    }

    /*private boolean isGeneratedCoordDifferenceMinTwo(Coordinate coordinate) {
        int x = coordinate.getX();
        int y = coordinate.getY();

        for (Coordinate secCoord : blackCoordinates) {
            int xDiff = Math.abs(x - secCoord.getX());
            int yDiff = Math.abs(y - secCoord.getY());
            if ((xDiff == 0 && yDiff < 3) || (yDiff == 0 && xDiff < 3)) {
                return false;
            }
        }
        return true;
    }

     */


    private void generateRndBlacks() {
        //Ha GRID_SIZE fekete van és csak egyetlen egy van minden sorban és oszlopban,akk nincs üres sor
        for (int i = 0; i < GRID_SIZE; i++) {
            while (true) {
                int x = rnd.nextInt(GRID_SIZE);
                int y = i;
                Coordinate coordinate = new Coordinate(x,y);
                if (!isRowOrColAlreadyContainsBlackV2(coordinate) && isGeneratedCoordDifferenceMinTwoV3(coordinate)) {
                    insertBlack(coordinate);
                    break;
                }
            }
        }
    }


    public void insertBlack(Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        if (isCoordinateBlack(coord)) {
            throw new IllegalStateException("Fekete kockára írás hiba.");
        }
        table[y][x] = BLACK_GRID_PLACEHOLDER;
    }

    public boolean isGeneratedCoordDifferenceMinTwoV3(Coordinate coordinate) {
        for (int i = -BLACKS_MIN_DISTANCE; i <= BLACKS_MIN_DISTANCE ; i++) {
            int tempX = coordinate.getX() + i;
            if (tempX < 0) {
                continue;
            }
            if (tempX >= GRID_SIZE) {
                break;
            }
            if (isCoordinateBlack(coordinate.getX() + i, coordinate.getY())) {
                return false;
            }
        }
        for (int j = -BLACKS_MIN_DISTANCE; j < BLACKS_MIN_DISTANCE; j++) {
            int tempY = coordinate.getY() + j;
            if (tempY < 0) {
                continue;
            }
            if (tempY >= GRID_SIZE) {
                break;
            }
            if (isCoordinateBlack(coordinate.getX(), coordinate.getY() + j)) {
                return false;
            }
        }
        return true;
    }

    public boolean isGeneratedCoordDifferenceMinTwoV4(Coordinate coordinate) {
        for (int i = -BLACKS_MIN_DISTANCE; i <= BLACKS_MIN_DISTANCE ; i++) {
            int tempX = coordinate.getX() + i;
            int tempY = coordinate.getY() + i;
            if (tempX < 0 || tempY < 0) {
                continue;
            }
            if (tempX >= GRID_SIZE) {
                break;
            }
            if (isCoordinateBlack(tempX, coordinate.getY()) || isCoordinateBlack(coordinate.getX(), tempY)) {
                return false;
            }
        }
        for (int j = -BLACKS_MIN_DISTANCE; j < BLACKS_MIN_DISTANCE; j++) {
            int tempY = coordinate.getY() + j;
            if (tempY < 0) {
                continue;
            }
            if (tempY >= GRID_SIZE) {
                break;
            }
            if (isCoordinateBlack(coordinate.getX(), coordinate.getY() + j)) {
                return false;
            }
        }
        return true;
    }


    private boolean isRowOrColAlreadyContainsBlackV2(Coordinate coord) {
        for (int i = 0;i < GRID_SIZE; i++) {
            if (isCoordinateBlack(i, coord.getY()) || isCoordinateBlack(coord.getX(), i)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCoordinateBlack(Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        if (isCoordinateBlack(x, y)) {
            return true;
        }
        return false;
    }

    public boolean isCoordinateBlack(int x, int y) {
        if (BLACK_GRID_PLACEHOLDER.equals(table[y][x])) { //|| !EMPTY_GRID_PLACEHOLDER.equals(table[x][y])
            return true;
        }
        return false;
    }

    public void deleteCoordinate(int x,int y) {
        if (isCoordinateBlack(x, y)) {
            throw new IllegalStateException("Can't delete black coordinate.");
        }
        table[y][x] = EMPTY_GRID_PLACEHOLDER;
    }

    public void deleteCoordinate(Coordinate coordinate) {
        table[coordinate.getY()][coordinate.getX()] = EMPTY_GRID_PLACEHOLDER;
    }

    public String[][] getTable() {
        return table;
    }
}