package wordgame;

import java.util.*;

public class Table {

    private static final int GRID_SIZE = 10;
    private static final String BLACK_GRID_PLACEHOLDER = "####";
    private static final int MIN_BLACK_SQUARES = 10;
    private static final int RANDOM_PLUS_BLACK_SQUARES = 10;
    private static final Random RND = new Random();

    private List<Coordinate> coordinates = new ArrayList<>();
    private String[][] table;

    public Table() {
        table = new String[GRID_SIZE][GRID_SIZE];
        makeBlackSquares();
    }

    private void makeBlackSquares() {
        int numberOfBlackSquares = MIN_BLACK_SQUARES + RND.nextInt(RANDOM_PLUS_BLACK_SQUARES) + 1;
        for (int i = 0; i < numberOfBlackSquares; i++) {
            Coordinate coord = generateCoordinate();
            table[coord.getxCoord()][coord.getyCoord()] = BLACK_GRID_PLACEHOLDER;
        }
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

        if (x == 1 || x == GRID_SIZE - 2 || y == 1 || y == GRID_SIZE - 2) {
            return false;
        }

        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate secCoord = coordinates.get(i);

            int xDiff = Math.abs(x - secCoord.getxCoord());
            int yDiff = Math.abs(y - secCoord.getyCoord());

            if ((xDiff == 0 && yDiff < 3) || (yDiff == 0 && xDiff < 3)) {
                return false;
            }
        }

        coordinates.add(coordinate);
        return true;
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
