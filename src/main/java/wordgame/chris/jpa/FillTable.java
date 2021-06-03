package wordgame.chris.jpa;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public class FillTable {

    private Table2 table;
    private WordGameJpaDAO wordGameJpaDAO = new WordGameJpaDAO();

    public FillTable(Table2 table) {
        this.table = table;
    }

    public Table2 fillWords(Table2 table) {
        List<WordStartingCoordinate> startingCoordinates = findAllStartingCoordinates();
        ListIterator<WordStartingCoordinate> lit = startingCoordinates.listIterator();

        while (lit.hasNext()) {
            boolean found = false;
            WordStartingCoordinate wordStartingCoordinate = lit.next();
            wordStartingCoordinate.deleteCharAtCoordinates(table);
            List<String> words = wordGameJpaDAO
                    .queryWordsWithLenghtAndLike(wordLengthFromStartingCoordinate(wordStartingCoordinate), likePatternMaker(wordStartingCoordinate));

            for (String item : words) {
                if (wordStartingCoordinate.getWords().contains(item)) {
                    continue;
                }
                if (isWordWritable(item, wordStartingCoordinate)) {
                    List<Coordinate> charCoords = fillWordFromCoordinate(item, wordStartingCoordinate);
                    wordStartingCoordinate.addCoordinates(charCoords);
                    wordStartingCoordinate.addWord(item);
                    found = true;
                    break;
                }
            }
            if (found == false) {
                wordStartingCoordinate.clearWords();
                wordStartingCoordinate.deleteCharAtCoordinates(table);
                lit.previous();
                lit.previous();
            }
        }
        return table;
    }

    public String getWordFromStartingCoordinate(WordStartingCoordinate coordinate) {
        String word = "";
        Alignment alignment = coordinate.getAlignment();
        int incCoord = (alignment == Alignment.HORISONTAL) ? coordinate.getX() : coordinate.getY();
        for (int i = incCoord; i < table.GRID_SIZE; i++) {
            if (table.isCoordinateBlack(coordinate.getX(), coordinate.getY())) {
                return word;
            }
            word += readCharacterAtCoordinate(coordinate);
            coordinate = incrementCoordinate(coordinate);
        }
        return word;
    }

    public boolean isEmptyCoordinate(Coordinate coordinate) {
        if (table.EMPTY_GRID_PLACEHOLDER.equals(readCharacterAtCoordinate(coordinate))) {
            return true;
        }
        return false;
    }

    public void insertCharacter(String s, Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        if (table.isCoordinateBlack(coord) || (!s.equals(readCharacterAtCoordinate(coord)) && !isEmptyCoordinate(coord))) {
            throw new IllegalStateException("Írás hiba.");
        }
        table.getTable()[y][x] = s;
    }

    public List<Coordinate> fillWordFromCoordinate(String s, WordStartingCoordinate coord) {
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

    public boolean isWordWritable(String s, WordStartingCoordinate coord) {
        for (int i = 0; i < s.length(); i++) {
            String ch = String.valueOf(s.charAt(i));
            if (table.isCoordinateBlack(coord) || (!ch.equals(readCharacterAtCoordinate(coord)) && !isEmptyCoordinate(coord))) {
                return false;
            }
            coord = incrementCoordinate(coord);
        }
        return true;
    }

    public String likePatternMaker(WordStartingCoordinate coordinate) {
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

    public int wordLengthFromStartingCoordinate(WordStartingCoordinate coordinate) {
        Alignment alignment = coordinate.getAlignment();
        int incCoord = (alignment == Alignment.HORISONTAL) ? coordinate.getX() : coordinate.getY();
        for (int i = incCoord; i < table.GRID_SIZE; i++) {
            if (table.isCoordinateBlack(coordinate.getX(), coordinate.getY())) {
                return i - incCoord;
            }
            coordinate = incrementCoordinate(coordinate);
        }
        return table.GRID_SIZE - incCoord;
    }

    private WordStartingCoordinate incrementCoordinate(WordStartingCoordinate coordinate) {
        Alignment alignment = coordinate.getAlignment();
        if (alignment == Alignment.HORISONTAL) {
            return new WordStartingCoordinate(coordinate.getX() + 1, coordinate.getY(), alignment);
        }
        return new WordStartingCoordinate(coordinate.getX(), coordinate.getY() + 1, alignment);
    }

    public List<WordStartingCoordinate> findAllStartingCoordinates() {
        List<WordStartingCoordinate> coordinates = horisontalStartingCoorinates();
        coordinates.addAll(verticalStartingCoordinates());
        coordinates.sort(Comparator.comparingInt(Coordinate::getY).thenComparing(Coordinate::getX));
        return coordinates;
    }

    private List<WordStartingCoordinate> horisontalStartingCoorinates() {
        Alignment alignment = Alignment.HORISONTAL;
        List<WordStartingCoordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < table.GRID_SIZE; i++) {
            //Ha az új sor nem fekete kocka,akkor új szó kezdődik
            if (!table.isCoordinateBlack(0, i)) {
                coordinates.add(new WordStartingCoordinate(0, i, alignment));
            }
            //Ha az előző sorban volt betű
            for (int j = 0; j < table.GRID_SIZE; j++) {
                //Ha fekete kockával kezdődik az új sor,akkor a szó utána kezdődik
                if (table.isCoordinateBlack(j, i) && j + 1 < table.GRID_SIZE) {
                    coordinates.add(new WordStartingCoordinate(j + 1, i, alignment));
                }
            }
        }
        return coordinates;
    }

    private List<WordStartingCoordinate> verticalStartingCoordinates() {
        Alignment alignment = Alignment.VERTICAL;
        List<WordStartingCoordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < table.GRID_SIZE; i++) {
            //Ha az új sor nem fekete kocka,akkor új szó kezdődik
            if (!table.isCoordinateBlack(i, 0)) {
                coordinates.add(new WordStartingCoordinate(i, 0, alignment));
            }
            for (int j = 0; j < table.GRID_SIZE; j++) {
                //Ha fekete kockával kezdődik az új sor,akkor a szó utána kezdődik
                if (table.isCoordinateBlack(i, j) && j + 1 < table.GRID_SIZE) {
                    coordinates.add(new WordStartingCoordinate(i, j + 1, alignment));
                }

            }
        }
        return coordinates;
    }

    public String readCharacterAtCoordinate(Coordinate coordinate) {
        return table.getTable()[coordinate.getY()][coordinate.getX()];
    }

    public void printTable() {
        for (String[] strArr : table.getTable()) {
            for (String str : strArr) {
                System.out.print(str + " ");
            }
            System.out.println();
        }
    }

    public void deleteCoordinate(int x,int y) {
        table.deleteCoordinate(x,y);
    }

    public Table2 getTable() {
        return table;
    }

    public WordGameJpaDAO getWordGameJpaDAO() {
        return wordGameJpaDAO;
    }
}
