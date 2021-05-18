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

    public void func(List<Coordinate> coordinates, List<Coordinate> coordinatesVer) {
        VerAndHorWordsLengthsFromCoordinates verAndHorWordsLengthsFromCoordinates = new VerAndHorWordsLengthsFromCoordinates(coordinates, coordinatesVer);
        Stack<String> stack = new Stack<>();
        ListIterator<Coordinate> lit = verAndHorWordsLengthsFromCoordinates.getCoordinates().listIterator();
        Coordinate coordinate2;



        while (lit.hasNext()) {
            coordinate2 = lit.next();
            boolean found = false;
            List<String> stringList = new WordGameDAO().queryWordsWithLenghtAndLike(wordLengthFromStartingCoordinate(coordinate2), likePatternMaker(coordinate2));
            if (stringList.isEmpty()) {
                //stack.pop();
                coordinate2.clearWords();
                lit.previous();
                continue;
            }
            for (String item2 : stringList) {
                if (stack.contains(item2)) {
                    continue;
                }
                if (checkStringWrite(item2, coordinate2)) {
                    stack.push(item2);
                    coordinate2.addWord(item2);
                    found = true;
                    break;
                }
                continue;
            }
            if (!found) {
                //stack.pop();
               coordinate2.clearWords();
                lit.previous();
                continue;
            }



        }

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
        if (isCoordinateFilled(coord)) {
            throw new IllegalStateException("Fekete kockára írás hiba.");
        }
        table[x][y] = BLACK_GRID_PLACEHOLDER;
    }

    private boolean isCoordinateFilled(Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        if (BLACK_GRID_PLACEHOLDER.equals(table[x][y])) { //|| !EMPTY_GRID_PLACEHOLDER.equals(table[x][y])
            return true;
        }
        return false;
    }

    private boolean isCoordinateBlack(Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        if (BLACK_GRID_PLACEHOLDER.equals(table[y][x])) { //|| !EMPTY_GRID_PLACEHOLDER.equals(table[x][y])
            return true;
        }
        return false;
    }

    public void insertString(String s, Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        if (isCoordinateFilled(coord) || !s.equals(table[y][x])) {
           throw new IllegalStateException("Írás hiba.");
        }
        table[y][x] = s;
    }

    public void fillWordFromCoordinate(String s, Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        for (int i = 0; i < s.length(); i++) {
                insertString(String.valueOf(s.charAt(i)), new Coordinate(x, y));
            if (coord.getAlignment().equals(Alignment.VERTICAL)) {
                y++;
            } else {
                x++;
            }
        }
    }

    public boolean checkStringWrite(String s, Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        for (int i = 0; i < s.length(); i++) {
                if (isCoordinateBlack(coord) || (!s.equals(table[y][x]) && !EMPTY_GRID_PLACEHOLDER.equals(table[y][x]))) {
                    return false;
                }
            if (coord.getAlignment().equals(Alignment.VERTICAL)) {
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
            if (EMPTY_GRID_PLACEHOLDER.equals(table[y][x])) {
                like += "_";
            } else {
                like += table[y][x];
            }
            if (alignment.equals(Alignment.VERTICAL)) {
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

    public List<Coordinate> horisontalStartingCoorinates() {
        Alignment alignment = Alignment.HORISONTAL;
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            //Ha az új sor nem fekete kocka,akkor új szó kezdődik
            if (!BLACK_GRID_PLACEHOLDER.equals(table[i][0])) {
                coordinates.add(new Coordinate(0, i, alignment));
            }
            //Ha az előző sorban volt betű
            for (int j = 0; j < GRID_SIZE; j++) {
                //Ha fekete kockával kezdődik az új sor,akkor a szó utána kezdődik
                if (BLACK_GRID_PLACEHOLDER.equals(table[i][j]) && j + 1 < GRID_SIZE) {
                    coordinates.add(new Coordinate(j + 1, i, alignment));
                }
            }
        }
        return coordinates;
    }

    public int wordLengthFromStartingCoordinate(Coordinate coordinate) {
        int x = coordinate.getX();
        int y = coordinate.getY();
        Alignment alignment = coordinate.getAlignment();
        int incCoord = (alignment.equals(Alignment.HORISONTAL)) ? x : y;
        for (int i = incCoord; i < GRID_SIZE; i++) {
            if (table[y][x].equals(BLACK_GRID_PLACEHOLDER)) {
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

    public List<Coordinate> verticalStartingCoordinates() {
        Alignment alignment = Alignment.VERTICAL;
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {


            //Ha az új sor nem fekete kocka,akkor új szó kezdődik
            if (!BLACK_GRID_PLACEHOLDER.equals(table[0][i])) {
                coordinates.add(new Coordinate(i, 0, alignment));

            }
            for (int j = 0; j < GRID_SIZE; j++) {
                //Ha fekete kockával kezdődik az új sor,akkor a szó utána kezdődik
                if (BLACK_GRID_PLACEHOLDER.equals(table[j][i]) && j + 1 < GRID_SIZE) {
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
