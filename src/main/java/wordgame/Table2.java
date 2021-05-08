package wordgame;

import java.util.*;

public class Table2 {

    private static final int GRID_SIZE = 20;
    private static final String BLACK_GRID_PLACEHOLDER = "####";
    private static final int MIN_BLACK_SQUARES = GRID_SIZE * GRID_SIZE / 10;
    private static final Random RND = new Random();
    private static final int NUM_OF_BLACK_SQUARES = MIN_BLACK_SQUARES + RND.nextInt(MIN_BLACK_SQUARES);

    private List<Coordinate> coordinates = new ArrayList<>();
    private String[][] table;

    public Table2() {
        table = new String[GRID_SIZE][GRID_SIZE];
        makeBlackSquares();
    }

    private void makeBlackSquares() {
        while (coordinates.size() < GRID_SIZE) {
            Coordinate coordinate = generateCoordinate(coordinates.size());
            coordinates.add(coordinate);
        }

        for (Coordinate coord: coordinates) {
            table[coord.getxCoord()][coord.getyCoord()] = BLACK_GRID_PLACEHOLDER;
        }
    }

    private Coordinate generateCoordinate(int size) {
        int x = RND.nextInt(GRID_SIZE);
        int y = size;
        Coordinate coord = new Coordinate(x, y);

        if (isGeneratedCoordPosGood(coord)) {
            return coord;
            //coordinates.add(coord);
        }
            /*x = i;
            y = RND.nextInt(GRID_SIZE);
            coord = new Coordinate(x,y);
            if (isGeneratedCoordPosGood(coord))
            {
                coordinates.add(coord);
            }

             */
        return generateCoordinate(size);
    }

    private boolean isGeneratedCoordPosGood(Coordinate coordinate) {
        int x = coordinate.getxCoord();
        int y = coordinate.getyCoord();

        /*if (x == 1 || x == GRID_SIZE - 2 || y == 1 || y == GRID_SIZE - 2) {
            return false;
        }
         */

        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate secCoord = coordinates.get(i);

            int xDiff = Math.abs(x - secCoord.getxCoord());
            int yDiff = Math.abs(y - secCoord.getyCoord());

            if ((xDiff == 0 && yDiff < 3) || (yDiff == 0 && xDiff < 3)) {
                return false;
            }
        }

        //coordinates.add(coordinate);
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

    /*private boolean isAllRowColContainsBlackSquare() {
        for (Coordinate item : coordinates) {
            int x = item.getxCoord();
            int y = item.getyCoord();
            if ()
        }
    }

     */
}
