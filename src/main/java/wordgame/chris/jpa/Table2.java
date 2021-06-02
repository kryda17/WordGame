package wordgame.chris.jpa;

import java.util.*;

public class Table2 {

    public final int GRID_SIZE;
    public final String EMPTY_GRID_PLACEHOLDER = "0";
    public final String BLACK_GRID_PLACEHOLDER = "#";
    private final int MIN_BLACK_SQUARES; //Hogy minden sorban és oszlopban legyen egy,az egyenlő a GRID_SIZE
    private final int MIN_ADDITIONAL_BLACK_SQUARE;

    private String[][] table;
    private Random rnd;
    private List<Coordinate> blackCoordinates = new ArrayList<>();

    public Table2(int SIZE, Random rnd) {
        GRID_SIZE = SIZE;
        MIN_BLACK_SQUARES = GRID_SIZE;
        table = new String[GRID_SIZE][GRID_SIZE];
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
                deleteCoordinate(new Coordinate(i, j));
            }
        }
    }

    private void makeBlackSquares() {
        generateRndBlacks();
        generateAdditionalRandomBlacks();
        placeAllGeneratedBlacks();
    }

    private void placeAllGeneratedBlacks() {
        for (int i = 0; i < blackCoordinates.size(); i++) {
            insertBlackSquare(blackCoordinates.get(i));
        }
    }

    public boolean isCoordinateBlack(Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        if (isCoordinateBlack(x, y)) { //|| !EMPTY_GRID_PLACEHOLDER.equals(table[x][y])
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

    private void insertBlackSquare(Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        if (isCoordinateBlack(coord)) {
            throw new IllegalStateException("Fekete kockára írás hiba.");
        }
        table[y][x] = BLACK_GRID_PLACEHOLDER;
    }



    private void generateAdditionalRandomBlacks() {
        int counter = 0; //ideiglenes
        int rnd_num_of_black_squares = rnd.nextInt(MIN_BLACK_SQUARES) + MIN_ADDITIONAL_BLACK_SQUARE;
        int allRequiredBlackSquare = MIN_BLACK_SQUARES + rnd_num_of_black_squares;
        while (blackCoordinates.size() < allRequiredBlackSquare) {
            ++counter; //ideiglenes
            int x = rnd.nextInt(GRID_SIZE);
            int y = rnd.nextInt(GRID_SIZE);
            Coordinate coordinate = new Coordinate(x,y);
            if (isGeneratedCoordDifferenceMinTwo(coordinate)) {
                blackCoordinates.add(coordinate);
            }
        }
        System.out.println(rnd_num_of_black_squares + " random fekete kocka generálása pluszba még: " +       //ideiglenes
                + counter + " iteráció ---  Koordinátákat tartalmazó lista mérete: " + blackCoordinates.size());    //ideiglenes
        System.out.println();  //ideiglenes
    }



    private boolean isGeneratedCoordDifferenceMinTwo(Coordinate coordinate) {
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

    private boolean isRowColAlreadyContainsBlack(Coordinate coord) {
        for (Coordinate secCoord : blackCoordinates) {
            if ((coord.getX() == secCoord.getX()) || (coord.getY() == secCoord.getY())) {
                return true;
            }
        }
        return false;
    }

    private void generateRndBlacks() {
        int counter = 0; //ideiglenes
        //Ha GRID_SIZE fekete van és csak egyetlen egy van minden sorban és oszlopban,akk nincs üres sor
        for (int i = 0; i < GRID_SIZE; i++) {
            while (true) {
                ++counter; //ideiglenes
                int x = rnd.nextInt(GRID_SIZE);
                int y = i;
                Coordinate coordinate = new Coordinate(x,y);
                if (!isRowColAlreadyContainsBlack(coordinate) && isGeneratedCoordDifferenceMinTwo(coordinate)) {
                    blackCoordinates.add(coordinate);
                    break;
                }
            }
        }
        System.out.println(GRID_SIZE + " fekete kocka: " + counter +    //ideiglenes
                " iteráció ---  Koordinátákat tartalmazó lista mérete: " + blackCoordinates.size());     //ideiglenes
    }

    public void deleteCoordinate(Coordinate coordinate) {
        table[coordinate.getY()][coordinate.getX()] = EMPTY_GRID_PLACEHOLDER;
    }

    public String[][] getTable() {
        return table;
    }
}