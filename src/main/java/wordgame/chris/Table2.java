package wordgame.chris;

import java.util.*;

public class Table2 {

    public static final int GRID_SIZE = 15;
    public static final String EMPTY_GRID_PLACEHOLDER = "0";
    private static final String BLACK_GRID_PLACEHOLDER = "#";
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
        int counter = 0;
        List<Coordinate> startingCoordinates = findAllStartingCoordinates();
        ListIterator<Coordinate> lit = startingCoordinates.listIterator();
        Example example = new Example(startingCoordinates);

        while (lit.hasNext()) {
            boolean found = false;
            Coordinate coordinate = lit.next();
            example.get(coordinate).deleteCharAtCoordinates(table);
            List<String> words = new WordGameDAO().queryWordsWithLenghtAndLike(wordLengthFromStartingCoordinate(coordinate), likePatternMaker(coordinate));

            ++counter;
            for (String item : words) {
                if (example.get(coordinate).getWords().contains(item)) {
                    continue;
                }
                if (isWordWritable(item, coordinate)) {
                    List<Coordinate> charCoords = fillWordFromCoordinate(item, coordinate);
                    example.get(coordinate).addCoordinates(charCoords);
                    example.get(coordinate).addWord(item);
                    found = true;
                    break;
                }
            }
            if (found == false) {
                example.get(coordinate).clearWords();
                example.get(coordinate).deleteCharAtCoordinates(table);
                lit.previous();
                lit.previous();
            }
        }
        System.out.println(counter);
        //throw new IllegalStateException("Vége");
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
        if (BLACK_GRID_PLACEHOLDER.equals(readStringAtCoordinate(new Coordinate(x, y)))) { //|| !EMPTY_GRID_PLACEHOLDER.equals(table[x][y])
            return true;
        }
        return false;
    }

    private boolean isEmptyCoordinate(int x, int y) {
        if (EMPTY_GRID_PLACEHOLDER.equals(readStringAtCoordinate(new Coordinate(x, y)))) {
            return true;
        }
        return false;
    }

    public void insertCharacter(String s, Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        if (isCoordinateBlack(coord) || (!s.equals(readStringAtCoordinate(new Coordinate(x, y))) && !isEmptyCoordinate(x, y))) {
           throw new IllegalStateException("Írás hiba.");
        }
        table[y][x] = s;
    }

    public List<Coordinate> fillWordFromCoordinate(String s, Coordinate coord) {
        List<Coordinate> coordinates = new ArrayList<>();
        int x = coord.getX();
        int y = coord.getY();
        for (int i = 0; i < s.length(); i++) {
            String ch = String.valueOf(s.charAt(i));
                if (isEmptyCoordinate(x, y)) {
                    coordinates.add(new Coordinate(x,y));
                }
            insertCharacter(ch, new Coordinate(x, y));
            if (coord.getAlignment() == Alignment.VERTICAL) {
                y++;
            } else {
                x++;
            }
        }
        return coordinates;
    }

    public boolean isWordWritable(String s, Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        for (int i = 0; i < s.length(); i++) {
            String ch = String.valueOf(s.charAt(i));
                if (isCoordinateBlack(new Coordinate(x,y)) || (!ch.equals(readStringAtCoordinate(new Coordinate(x, y))) && !isEmptyCoordinate(x, y))) {
                    return false;
                }
            if (coord.getAlignment() == Alignment.VERTICAL) {
                y++;
            } else {
                x++;
            }
        }
        return true;
    }

    public String likePatternMaker(Coordinate coordinate) {
        Alignment alignment = coordinate.getAlignment();
        int x = coordinate.getX();
        int y = coordinate.getY();
        String like = "";
        for (int i = 0; i < wordLengthFromStartingCoordinate(coordinate); i++) {
            if (isEmptyCoordinate(x, y)) {
                like += "_";
            } else {
                like += readStringAtCoordinate(new Coordinate(x, y));
            }
            if (alignment == Alignment.VERTICAL) {
                y++;
            } else {
                x++;
            }
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
        int x = coordinate.getX();
        int y = coordinate.getY();
        Alignment alignment = coordinate.getAlignment();
        int incCoord = (alignment == Alignment.HORISONTAL) ? x : y;
        for (int i = incCoord; i < GRID_SIZE; i++) {
            if (isCoordinateBlack(x, y)) {
                return i - incCoord;
            }
            if (alignment.equals(Alignment.HORISONTAL)) {
                ++x;
            } else {
                ++y;
            }
        }
        return GRID_SIZE - incCoord;
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

    public String readStringAtCoordinate(Coordinate coordinate) {
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
