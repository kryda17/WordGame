package wordgame.chris.jdbc;

import java.util.*;

public class Table2 {

    public static final int GRID_SIZE = 15;
    public static final String EMPTY_GRID_PLACEHOLDER = "0";
    public static final String BLACK_GRID_PLACEHOLDER = "#";
    private final int MIN_BLACK_SQUARES = GRID_SIZE; //Hogy minden sorban és oszlopban legyen egy,az egyenlő a GRID_SIZE
    private final int MIN_ADDITIONAL_BLACK_SQUARE = GRID_SIZE / 2;

    private List<Coordinate> blackCoordinates = new ArrayList<>();
    private String[][] table = new String[GRID_SIZE][GRID_SIZE];
    private Random rnd = new Random();

    public Table2() {
        initTable();
    }

    public Table2(Random rnd) {
        this.rnd = rnd;
        initTable();
    }

    public void initTable() {
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


    public void func() {
       // try {
            int counter = 0;
            List<Coordinate> startingCoordinates = findAllStartingCoordinates();
            ListIterator<Coordinate> lit = startingCoordinates.listIterator();
            Example example = new Example(startingCoordinates);

            while (lit.hasNext()) {
                boolean found = false;
                Coordinate coordinate = lit.next();
                example.deleteCharAtCoordinates(table, coordinate);
                List<String> words = new WordGameJdbcDAO().queryWordsWithLenghtAndLike(wordLengthFromStartingCoordinate(coordinate), likePatternMaker(coordinate));

                ++counter;
                for (String item : words) {
                    if (example.getWords(coordinate).contains(item)) {
                        continue;
                    }
                    if (isWordWritable(item, coordinate)) {
                        List<Coordinate> charCoords = fillWordFromCoordinate(item, coordinate);
                        example.addCoordinates(charCoords, coordinate);
                        example.addWord(item, coordinate);
                        found = true;
                        break;
                    }
                }
                if (found == false) {
                    example.clearWords(coordinate);
                    example.deleteCharAtCoordinates(table, coordinate);
                    lit.previous();
                    lit.previous();
                }
            }
            System.out.println(counter);
       /* } catch (NoSuchElementException nsee) {
            throw new IllegalStateException("Az adatbázisban található szavakból nem lehet keresztrejtvényt összeállítani.", nsee);
        }

        */
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
        int x = coord.getX();
        int y = coord.getY();
        if (isCoordinateBlack(coord)) {
            throw new IllegalStateException("Fekete kockára írás hiba.");
        }
        table[y][x] = BLACK_GRID_PLACEHOLDER;
    }

    private boolean isCoordinateBlack(Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        if (isCoordinateBlack(x, y)) { //|| !EMPTY_GRID_PLACEHOLDER.equals(table[x][y])
            return true;
        }
        return false;
    }

    private boolean isCoordinateBlack(int x, int y) {
        if (BLACK_GRID_PLACEHOLDER.equals(readCharacterAtCoordinate(new Coordinate(x, y)))) { //|| !EMPTY_GRID_PLACEHOLDER.equals(table[x][y])
            return true;
        }
        return false;
    }

    private boolean isEmptyCoordinate(Coordinate coordinate) {
        if (EMPTY_GRID_PLACEHOLDER.equals(readCharacterAtCoordinate(coordinate))) {
            return true;
        }
        return false;
    }

    public void insertCharacter(String s, Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        if (isCoordinateBlack(coord) || (!s.equals(readCharacterAtCoordinate(coord)) && !isEmptyCoordinate(coord))) {
           throw new IllegalStateException("Írás hiba.");
        }
        table[y][x] = s;
    }

    public List<Coordinate> fillWordFromCoordinate(String s, Coordinate coord) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < s.length(); i++) {
            String ch = String.valueOf(s.charAt(i));
                if (isEmptyCoordinate(coord)) {
                    coordinates.add(coord);
                }
            insertCharacter(ch, coord);
            coord = incrementCoordinate(coord);
        }
        return coordinates;
    }

    public boolean isWordWritable(String s, Coordinate coord) {
        for (int i = 0; i < s.length(); i++) {
            String ch = String.valueOf(s.charAt(i));
                if (isCoordinateBlack(coord) || (!ch.equals(readCharacterAtCoordinate(coord)) && !isEmptyCoordinate(coord))) {
                    return false;
                }
           coord = incrementCoordinate(coord);
        }
        return true;
    }

    public String likePatternMaker(Coordinate coordinate) {
        int wordlength = wordLengthFromStartingCoordinate(coordinate);
        String like = "";
        for (int i = 0; i < wordlength; i++) {
            if (isEmptyCoordinate(coordinate)) {
                like += "_";
            } else {
                like += readCharacterAtCoordinate(coordinate);
            }
            coordinate = incrementCoordinate(coordinate);
        }
        return like;
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

    public int wordLengthFromStartingCoordinate(Coordinate coordinate) {
        Alignment alignment = coordinate.getAlignment();
        int incCoord = (alignment == Alignment.HORISONTAL) ? coordinate.getX() : coordinate.getY();
        for (int i = incCoord; i < GRID_SIZE; i++) {
            if (isCoordinateBlack(coordinate.getX(), coordinate.getY())) {
                return i - incCoord;
            }
            coordinate = incrementCoordinate(coordinate);
        }
        return GRID_SIZE - incCoord;
    }

    private Coordinate incrementCoordinate(Coordinate coordinate) {
        Alignment alignment = coordinate.getAlignment();
        if (alignment == Alignment.HORISONTAL) {
            return new Coordinate(coordinate.getX() + 1, coordinate.getY(), alignment);
        }
            return new Coordinate(coordinate.getX(), coordinate.getY() + 1, alignment);
    }

    public List<Coordinate> findAllStartingCoordinates() {
        List<Coordinate> coordinates = horisontalStartingCoorinates();
        coordinates.addAll(verticalStartingCoordinates());
        coordinates.sort(Comparator.comparingInt(Coordinate::getY).thenComparing(Coordinate::getX));
        return coordinates;
    }

    private List<Coordinate> horisontalStartingCoorinates() {
        Alignment alignment = Alignment.HORISONTAL;
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            //Ha az új sor nem fekete kocka,akkor új szó kezdődik
            if (!isCoordinateBlack(0, i)) {
                coordinates.add(new Coordinate(0, i, alignment));
            }
            //Ha az előző sorban volt betű
            for (int j = 0; j < GRID_SIZE; j++) {
                //Ha fekete kockával kezdődik az új sor,akkor a szó utána kezdődik
                if (isCoordinateBlack(j, i) && j + 1 < GRID_SIZE) {
                    coordinates.add(new Coordinate(j + 1, i, alignment));
                }
            }
        }
        return coordinates;
    }

    private List<Coordinate> verticalStartingCoordinates() {
        Alignment alignment = Alignment.VERTICAL;
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            //Ha az új sor nem fekete kocka,akkor új szó kezdődik
            if (!isCoordinateBlack(i, 0)) {
                coordinates.add(new Coordinate(i, 0, alignment));
            }
            for (int j = 0; j < GRID_SIZE; j++) {
                //Ha fekete kockával kezdődik az új sor,akkor a szó utána kezdődik
                if (isCoordinateBlack(i, j) && j + 1 < GRID_SIZE) {
                    coordinates.add(new Coordinate(i, j + 1, alignment));
                }

            }
        }
        return coordinates;
    }

    public String readCharacterAtCoordinate(Coordinate coordinate) {
        return table[coordinate.getY()][coordinate.getX()];
    }

    public void printTable() {
        for (String[] strArr : table) {
            for (String str : strArr) {
                System.out.print(str + " ");
            }
            System.out.println();
        }
    }

    public String[][] getTable() {
        return table;
    }
}
