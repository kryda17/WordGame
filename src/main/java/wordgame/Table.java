package wordgame;

import java.util.Random;

public class Table {

    private static final int GRID_SIZE = 10;
    private static final String BLACK_GRID_PLACEHOLDER = "BLANK";
    private static final int MAX_BLACK_SQUARES = 10;
    private String[][] table;

    public Table() {
        table = new String[GRID_SIZE][GRID_SIZE];
        makeBlackSquares();
    }

    //Ha ugyan azokat adja véletlenszerűen,akk kevesebb fekete kocka lesz,első futtatásra 1 volt :D
    //Kellene egy új osztály szerintem,egy 2DPoint,vagy hasonló a koordinátáknak és
    //Egy ciklus,amit addig futtatni és addolni vele ezt a coordináta objektumot egy setbe,amig i el nem éri a set.size() == MAX_BLACK_SQUARES-t
    private void makeBlackSquares() {
        Random rnd = new Random();
        int numberOfBlackSquares = rnd.nextInt(MAX_BLACK_SQUARES) + 1;
        for(int i = 0; i<numberOfBlackSquares; i++){
            int xCoord = rnd.nextInt(GRID_SIZE);
            int yCoord = rnd.nextInt(GRID_SIZE);
            table[xCoord][yCoord] = BLACK_GRID_PLACEHOLDER;
        }
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
