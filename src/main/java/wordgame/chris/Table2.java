package wordgame.chris;

import java.util.*;

public class Table2 {

    public static final int GRID_SIZE = 15;
    private static final String BLACK_GRID_PLACEHOLDER = "#";
    private static final String EMPTY_GRID_PLACEHOLDER = "0";
    private static final int MIN_BLACK_SQUARES = GRID_SIZE; //Hogy minden sorban és oszlopban legyen egy,az egyenlő a GRID_SIZE
    private static final int MIN_ADDITIONAL_BLACK_SQUARE = GRID_SIZE / 2;

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
        int x = coord.getxCoord();
        int y = coord.getyCoord();
        if (isCoordinateFilled(coord) || (!s.equals(table[y][x]) && !table[y][x].equals(EMPTY_GRID_PLACEHOLDER))) {
           throw new IllegalStateException("Írás hiba.");
        }
        table[y][x] = s;
    }

    public void fillWordFromCoordinate(String s, Coordinate coord, Alignment alignment) {
        int x = coord.getxCoord();
        int y = coord.getyCoord();
        for (int i = 0; i < s.length(); i++) {
                insertString(String.valueOf(s.charAt(i)), new Coordinate(x, y));
            if (alignment.equals(Alignment.VERTICAL)) {
                y++;
            } else {
                x++;
            }
        }
    }

    public boolean checkStringWrite(String s, Coordinate coord, Alignment alignment) {
        int x = coord.getxCoord();
        int y = coord.getyCoord();
        for (int i = 0; i < s.length(); i++) {
                if (isCoordinateFilled(coord) || (!s.equals(table[y][x]) || !EMPTY_GRID_PLACEHOLDER.equals(s))) {
                    return false;
                }
            if (alignment.equals(Alignment.VERTICAL)) {
                y++;
            } else {
                x++;
            }
        }
        return true;
    }

    public String likePatternMaker(WordLengthFromCoordinate wordLengthFromCoordinate) {
        int x = wordLengthFromCoordinate.getCoordinate().getxCoord();
        int y = wordLengthFromCoordinate.getCoordinate().getyCoord();
        String like = "";
        for (int i = 0; i < wordLengthFromCoordinate.getWordLength(); i++) {
            if (EMPTY_GRID_PLACEHOLDER.equals(table[y][x])) {
                like += "_";
            } else {
                like += table[y][x];
            }
            if (wordLengthFromCoordinate.getAlignment().equals(Alignment.VERTICAL)) {
                y++;
            } else {
                x++;
            }
        }
        return like;
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

    public List<WordLengthFromCoordinate> requiredHorWordsLengthAndStartingCoord() {
        List<Coordinate> coordinates = new ArrayList<>();
        List<Integer> wordLength = new ArrayList<>();
            int counter = 0;
        for (int i = 0; i < GRID_SIZE; i++) {


            //Ha az új sor nem fekete kocka,akkor új szó kezdődik
            if (!BLACK_GRID_PLACEHOLDER.equals(table[i][0])) {
                Coordinate coordinateOut = new Coordinate(0, i);
                coordinates.add(coordinateOut);

            }
            //Ha az előző sorban volt betű
            if (counter > 0) {
                wordLength.add(counter);
                counter = 0;
            }
            for (int j = 0; j < GRID_SIZE; j++) {
                //Ha fekete kockával kezdődik az új sor,akkor a szó utána kezdődik
                if (BLACK_GRID_PLACEHOLDER.equals(table[i][j]) && j + 1 < GRID_SIZE) {
                    coordinates.add(new Coordinate(j + 1, i));
                    if (counter > 0) {
                        wordLength.add(counter);
                        counter = 0;
                    }
                    //continue;
                } else {
                    ++counter;
                }

            }
        }
        if (counter > 0) {
            wordLength.add(counter);
        }
        return mergeStartingCoordsWithWordsLength(coordinates, wordLength, Alignment.HORISONTAL);
    }


    public List<WordLengthFromCoordinate> requiredVerticalWordsLengthAndStartingCoord() {
        List<Coordinate> coordinates = new ArrayList<>();
        List<Integer> wordLength = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < GRID_SIZE; i++) {


            //Ha az új sor nem fekete kocka,akkor új szó kezdődik
            if (!BLACK_GRID_PLACEHOLDER.equals(table[0][i])) {
                Coordinate coordinateOut = new Coordinate(i, 0);
                coordinates.add(coordinateOut);

            }
            //Ha az előző sorban volt betű
            if (counter > 0) {
                wordLength.add(counter);
                counter = 0;
            }
            for (int j = 0; j < GRID_SIZE; j++) {
                //Ha fekete kockával kezdődik az új sor,akkor a szó utána kezdődik
                if (BLACK_GRID_PLACEHOLDER.equals(table[j][i]) && j + 1 < GRID_SIZE) {
                    coordinates.add(new Coordinate(i, j + 1));
                    if (counter > 0) {
                        wordLength.add(counter);
                        counter = 0;
                    }
                    //continue;
                } else {
                    ++counter;
                }

            }
        }
        if (counter > 0) {
            wordLength.add(counter);
        }
        return mergeStartingCoordsWithWordsLength(coordinates, wordLength, Alignment.VERTICAL);
    }
    
   /* public void func(VerAndHorWordsLengthsFromCoordinates coordinates) {
        WordGameDAO dao = new WordGameDAO();

        for (int i = 0; i < GRID_SIZE; i++) {
            List<WordLengthFromCoordinate> line = coordinates.howMany(i, coordinates.getWordLengthFromCoordinatesHorisontal());
            for (int j = 0; j < line.size(); j++) {
                List<String> words = dao.queryWordsWithLenght(line.get(j).getWordLength());
                fillLine()
                //fillWordFromCoordinate(coordinates.getWordLengthFromCoordinatesHorisontal().get(j).getCoordinate(), Alignment.HORISONTAL)
            }
            for (int k = 0; k < coordinates.howMany(i, coordinates.getWordLengthFromCoordinatesVertical()).size(); k++) {

            }
        }
    }

    public boolean fillLine(List<String> words, int loopNum, Coordinate coordinate, Alignment alignment) {
        for (String item : words) {
            if (checkStringWrite(item, ))
            for (int i = 0; i < ; i++) {

            }
            if (fillWordFromCoordinate(item,coordinate,alignment)) {

            }
        }
    }

     */

    public String readCharAtCoordinate(Coordinate coordinate) {
        return table[coordinate.getyCoord()][coordinate.getxCoord()];
    }

    //
    private List<WordLengthFromCoordinate> mergeStartingCoordsWithWordsLength(List<Coordinate> coordinates, List<Integer> wordLengths, Alignment alignment) {
        List<WordLengthFromCoordinate> wordLengthFromCoordinates = new ArrayList<>();
        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate coordinate = coordinates.get(i);
            int length = wordLengths.get(i);
            wordLengthFromCoordinates.add(new WordLengthFromCoordinate(coordinate, length, alignment));
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
