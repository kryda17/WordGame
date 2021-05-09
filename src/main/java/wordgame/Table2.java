package wordgame;

import java.util.*;

public class Table2 {

    private static final int GRID_SIZE = 10;
    private static final String BLACK_GRID_PLACEHOLDER = "####";
    private static final int MIN_BLACK_SQUARES = GRID_SIZE;
    private static final Random RND = new Random();
    private static final int RND_NUM_OF_BLACK_SQUARES = RND.nextInt(MIN_BLACK_SQUARES);


    private List<Coordinate> coordinates = new ArrayList<>();
    private String[][] table;

    public Table2() {
        table = new String[GRID_SIZE][GRID_SIZE];
        makeBlackSquares();
        //searchAndFillEmpytLines();
    }

    private void makeBlackSquares() {
        coordinates = genCoords();
        fillWithRandomBlacks();
        for (int i = 0; i < coordinates.size(); i++) {
            insertBlackSquare(coordinates.get(i));
        }
    }

    private void fillWithRandomBlacks() {
        int counter = 0;
        for (int i = 0; i < RND_NUM_OF_BLACK_SQUARES; i++) {
                while (true) {
                    ++counter;
                    int x = RND.nextInt(GRID_SIZE);
                    int y = RND.nextInt(GRID_SIZE);
                    Coordinate coordinate = new Coordinate(x,y);
                    if (isGeneratedCoordDifferenceMinTwo(coordinate)) {
                        coordinates.add(coordinate);
                        break;
                    }
                }
            }
        System.out.println(counter);
        }

    private void insertBlackSquare(Coordinate coord) {
        table[coord.getxCoord()][coord.getyCoord()] = BLACK_GRID_PLACEHOLDER;
    }

    private boolean isGeneratedCoordDifferenceMinTwo(Coordinate coordinate) {
        int x = coordinate.getxCoord();
        int y = coordinate.getyCoord();

        for (Coordinate secCoord : coordinates) {

            int xDiff = Math.abs(x - secCoord.getxCoord());
            int yDiff = Math.abs(y - secCoord.getyCoord());

            if ((xDiff == 0 && yDiff < 3) || (yDiff == 0 && xDiff < 3)) {
                return false;
            }
        }
        return true;
    }

private boolean isRowColAlreadyContainsBlack(Coordinate coord) {
        for (Coordinate secCoord : coordinates) {
            if ((coord.getyCoord() == secCoord.getyCoord()) || (coord.getxCoord() == secCoord.getxCoord())) {
                return true;
            }
        }
        return false;
}

    private List<Coordinate> genCoords() {
        int counter = 0;
        //Ha GRID_SIZE fekete van és csak egyetlen egy van minden sorban és oszlopban,akk nincs üres sor
        for (int i = 0; i < GRID_SIZE; i++) {
            while (true) {
                ++counter;
                int x = RND.nextInt(GRID_SIZE);
                int y = i;
                Coordinate coordinate = new Coordinate(x,y);
                if (!isRowColAlreadyContainsBlack(coordinate) && isGeneratedCoordDifferenceMinTwo(coordinate)) {
                    coordinates.add(coordinate);
                    break;
                }
            }
        }
        System.out.println(counter);
        return coordinates;
    }

    public void printTable() {
        for (String[] str : table) {
            for (String s : str) {
                System.out.print(s + " ");
            }
            System.out.println();
        }
    }
}
