package wordgame;

import java.util.*;

public class Table2 {

    private static final int GRID_SIZE = 10;
    private static final String BLACK_GRID_PLACEHOLDER = "####";
    private static final int MIN_BLACK_SQUARES = GRID_SIZE; //Hogy minden sorban és oszlopban legyen egy,az egyenlő a GRID_SIZE

    private List<Coordinate> coordinates = new ArrayList<>();
    private String[][] table = new String[GRID_SIZE][GRID_SIZE];
    private Random rnd = new Random();

    public Table2() {
        makeBlackSquares();
    }

    private void makeBlackSquares() {
        genCoords();
        fillWithRandomBlacks();
        for (int i = 0; i < coordinates.size(); i++) {
            insertBlackSquare(coordinates.get(i));
        }
    }

    private void fillWithRandomBlacks() {
        int counter = 0;
        int rnd_num_of_black_squares = rnd.nextInt(MIN_BLACK_SQUARES);
        for (int i = 0; i < rnd_num_of_black_squares; i++) {
                while (true) {
                    ++counter;
                    int x = rnd.nextInt(GRID_SIZE);
                    int y = rnd.nextInt(GRID_SIZE);
                    Coordinate coordinate = new Coordinate(x,y);
                    if (isGeneratedCoordDifferenceMinTwo(coordinate)) {
                        coordinates.add(coordinate);
                        break;
                    }
                }
            }
        System.out.println(rnd_num_of_black_squares + " random fekete kocka generálása pluszba még: " + counter + " iteráció");
        System.out.println();
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

    private void genCoords() {
        int counter = 0;
        //Ha GRID_SIZE fekete van és csak egyetlen egy van minden sorban és oszlopban,akk nincs üres sor
        for (int i = 0; i < GRID_SIZE; i++) {
            while (true) {
                ++counter;
                int x = rnd.nextInt(GRID_SIZE);
                int y = i;
                Coordinate coordinate = new Coordinate(x,y);
                if (!isRowColAlreadyContainsBlack(coordinate) && isGeneratedCoordDifferenceMinTwo(coordinate)) {
                    coordinates.add(coordinate);
                    break;
                }
            }
        }
        System.out.println(GRID_SIZE + " fekete kocka: " + counter + " iteráció");
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
