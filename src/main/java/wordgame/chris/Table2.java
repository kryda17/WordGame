package wordgame.chris;

import java.util.*;

public class Table2 {

    public static final int GRID_SIZE = 15;
    private static final String BLACK_GRID_PLACEHOLDER = "#";
    private static final String EMPTY_GRID_PLACEHOLDER = "0";
    private static final int MIN_BLACK_SQUARES = GRID_SIZE; //Hogy minden sorban és oszlopban legyen egy,az egyenlő a GRID_SIZE

    private List<Coordinate> blackCoordinates = new ArrayList<>();
    private String[][] table = new String[GRID_SIZE][GRID_SIZE];
    private Random rnd = new Random(1);

    public Table2() {
        fillTalbeWithEmptys();
        makeBlackSquares();
    }

    private void fillTalbeWithEmptys() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                table[i][j] = EMPTY_GRID_PLACEHOLDER;
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

    private void generateAdditionalRandomBlacks() {
        int counter = 0;
        int rnd_num_of_black_squares = rnd.nextInt(MIN_BLACK_SQUARES) + GRID_SIZE / 2;
        int allRequiredBlackSquare = MIN_BLACK_SQUARES + rnd_num_of_black_squares;
                while (blackCoordinates.size() < allRequiredBlackSquare) {
                    ++counter;
                    int x = rnd.nextInt(GRID_SIZE);
                    int y = rnd.nextInt(GRID_SIZE);
                    Coordinate coordinate = new Coordinate(x,y);
                    if (isGeneratedCoordDifferenceMinTwo(coordinate)) {
                        blackCoordinates.add(coordinate);
                    }
                }
        System.out.println(rnd_num_of_black_squares + " random fekete kocka generálása pluszba még: " + counter + " iteráció ---  Koordinátákat tartalmazó lista mérete: " + blackCoordinates.size());
        System.out.println();
        }

    private void insertBlackSquare(Coordinate coord) {
        int x = coord.getxCoord();
        int y = coord.getyCoord();
        if (isCoordinateFilled(coord)) {
            throw new IllegalStateException("Fekete kockára írás hiba.");
        }
        table[x][y] = BLACK_GRID_PLACEHOLDER;
    }

    private boolean isCoordinateFilled(Coordinate coord) {
        int x = coord.getxCoord();
        int y = coord.getyCoord();
        if (BLACK_GRID_PLACEHOLDER.equals(table[x][y])) { //|| !EMPTY_GRID_PLACEHOLDER.equals(table[x][y])
            return true;
        }
        return false;
    }

    public void insertString(String s, Coordinate coord) {
        /*if (isCoordinateFilled(coord)) {
            throw new IllegalStateException("Kitöltött kockára írás hiba.");
        }

         */
        table[coord.getxCoord()][coord.getyCoord()] = s;
    }

    public void fillWordFromCoordinate(String s, Coordinate coord, Alignment alignment) {
        int x = coord.getxCoord();
        int y = coord.getyCoord();
            if (alignment.equals(Alignment.VERTICAL)) {
                for (int i = 0; i < s.length(); i++) {
                    insertString(String.valueOf(s.charAt(i)), new Coordinate(x++,y));
                }
            } else {
                for (int i = 0; i < s.length(); i++) {
                    insertString(String.valueOf(s.charAt(i)), new Coordinate(x,y++));
                }
            }
    }

    private boolean isGeneratedCoordDifferenceMinTwo(Coordinate coordinate) {
        int x = coordinate.getxCoord();
        int y = coordinate.getyCoord();

        for (Coordinate secCoord : blackCoordinates) {

            int xDiff = Math.abs(x - secCoord.getxCoord());
            int yDiff = Math.abs(y - secCoord.getyCoord());

            if ((xDiff == 0 && yDiff < 3) || (yDiff == 0 && xDiff < 3)) {
                return false;
            }
        }
        return true;
    }

    private boolean isRowColAlreadyContainsBlack(Coordinate coord) {
           for (Coordinate secCoord : blackCoordinates) {
             if ((coord.getyCoord() == secCoord.getyCoord()) || (coord.getxCoord() == secCoord.getxCoord())) {
                 return true;
             }
          }
          return false;
    }

    private void generateRndBlacks() {
        int counter = 0;
        //Ha GRID_SIZE fekete van és csak egyetlen egy van minden sorban és oszlopban,akk nincs üres sor
        for (int i = 0; i < GRID_SIZE; i++) {
            while (true) {
                ++counter;
                int x = rnd.nextInt(GRID_SIZE);
                int y = i;
                Coordinate coordinate = new Coordinate(x,y);
                if (!isRowColAlreadyContainsBlack(coordinate) && isGeneratedCoordDifferenceMinTwo(coordinate)) {
                    blackCoordinates.add(coordinate);
                    break;
                }
            }
        }
        System.out.println(GRID_SIZE + " fekete kocka: " + counter + " iteráció ---  Koordinátákat tartalmazó lista mérete: " + blackCoordinates.size());
    }

    public List<WordLengthFromCoordinate> requiredHorWordsLength() {
        List<Coordinate> coordinates = new ArrayList<>();
        List<Integer> wordLength = new ArrayList<>();
            int counter = 0;
            for (int i = 0; i < GRID_SIZE; i++) {
                //Ha az új sor kezdete nem fekete kocka,akkor új szó kezdődik
                if (!Table2.BLACK_GRID_PLACEHOLDER.equals(table[i][0])) {
                    //lehet nem i + 1,hanem csak i
                    coordinates.add(new Coordinate(i+ 1, 0));
                }
                //Ha az előző sorban volt betű
                if (counter > 0) {
                    wordLength.add(counter);
                    counter = 0;
                }
                for (int j = 0; j < GRID_SIZE; j++) {
                    //Ha fekete kockával kezdődik az új sor,akkor a szó utána kezdődik
                    if (Table2.BLACK_GRID_PLACEHOLDER.equals(table[i][j])) {
                        Coordinate coordinate = new Coordinate(i, j + 1);
                        //Ha az sorban az utolsó kocka fekete,akk nem adja hozzá a koordinátát a szó kezdeteként
                        if (j != GRID_SIZE - 1) {
                            //Coordinate coordinate2 = new Coordinate(i, j + 1);
                            coordinates.add(coordinate);
                        }
                        if (counter > 0) {
                            wordLength.add(counter);
                            counter = 0;
                        }
                        continue;
                    }
                    ++counter;
                }
            }
        return func(coordinates, wordLength);
    }

    public List<WordLengthFromCoordinate> requiredVerticalWordsLength() {
        List<Coordinate> coordinates = new ArrayList<>();
        List<Integer> wordLength = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            //Ha az új sor nem fekete kocka,akkor új szó kezdődik
            if (!Table2.BLACK_GRID_PLACEHOLDER.equals(table[0][i])) {
                //lehet nem i + 1,hanem csak i
                coordinates.add(new Coordinate(0, i + 1));
            }
            //Ha az előző sorban volt betű
            if (counter > 0) {
                wordLength.add(counter);
                counter = 0;
            }
            for (int j = 0; j < GRID_SIZE; j++) {
                //Ha fekete kockával kezdődik az új sor,akkor a szó utána kezdődik
                if (Table2.BLACK_GRID_PLACEHOLDER.equals(table[j][i])) {
                    Coordinate coordinate = new Coordinate(j + 1, i);
                    if (j != GRID_SIZE - 1) {
                        //Coordinate coordinate2 = new Coordinate(i, j + 1);
                        coordinates.add(coordinate);
                    }
                    if (counter > 0) {
                        wordLength.add(counter);
                        counter = 0;
                    }
                    continue;
                }
                ++counter;
            }
        }
        return func(coordinates, wordLength);
    }

    //
    private List<WordLengthFromCoordinate> func(List<Coordinate> coordinates, List<Integer> len) {
        List<WordLengthFromCoordinate> wordLengthFromCoordinates = new ArrayList<>();
        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate coordinate = coordinates.get(i);
            int length = len.get(i);
            wordLengthFromCoordinates.add(new WordLengthFromCoordinate(coordinate, length));
        }
        return wordLengthFromCoordinates;
    }

    public void printTable() {
        for (String[] str : table) {
            for (String s : str) {
                System.out.print(s + " ");
            }
            System.out.println();
        }
    }

    public String[][] getTable() {
        return table;
    }
}
