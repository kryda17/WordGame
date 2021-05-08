package wordgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Table {


    private static final int GRID_SIZE = 10;
    private static final String BLACK_GRID_PLACEHOLDER = "#";
    private static final int MIN_BLACK_SQUARES = 10;
    private static final int RANDOM_PLUS_BLACK_SQUARES = 10;
    private static final Random RND = new Random();

    private List<Coordinate> coordinates;
    private String[][] table;

    public Table() {
        table = new String[GRID_SIZE][GRID_SIZE];
        fillCoordinateListWithInvalidSqaures();
        makeBlackSquares();
    }

    private void fillCoordinateListWithInvalidSqaures() {
        List<Coordinate> toAdd = List.of(new Coordinate(0, 1),
                new Coordinate(0, GRID_SIZE - 2),
                new Coordinate(1, 0),
                new Coordinate(1, GRID_SIZE - 1),
                new Coordinate(GRID_SIZE - 2, 0),
                new Coordinate(GRID_SIZE - 2, GRID_SIZE - 1),
                new Coordinate(GRID_SIZE - 1, GRID_SIZE - 2),
                new Coordinate(GRID_SIZE - 1, 1));
        coordinates = new ArrayList<>(toAdd);
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
        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate secCoord = coordinates.get(i);
            int xDiff = Math.abs(coordinate.getxCoord() - secCoord.getxCoord());
            int yDiff = Math.abs(coordinate.getyCoord() - secCoord.getyCoord());
            if (xDiff < 2 || yDiff < 2) {
                return false;
            }
        }
        coordinates.add(coordinate);
        return true;
    }

    private boolean isValidCoord(Coordinate coord) {
        int x = coord.getxCoord();
        int y = coord.getyCoord();

        if (BLACK_GRID_PLACEHOLDER.equals(table[x][y])) {
            return false;
        }

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
