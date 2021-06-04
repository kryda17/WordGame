package wordgame.chris.jpa;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WordStartingCoordinate extends Coordinate {

    private Alignment alignment;

    private List<String> words = new ArrayList<>();
    private List<Coordinate> coordinates = new ArrayList<>();

    public WordStartingCoordinate(int x, int y, Alignment alignment) {
        super(x, y);
        this.alignment = alignment;
    }

    public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Alignment getAlignment() {
            return alignment;
        }

    public void addWord(String word) {
        words.add(word);
    }

    public void clearWords() {
        words.clear();
    }

    public List<String> getWords() {
        return List.copyOf(words);
    }

    public List<Coordinate> getCoordinates() {
        return List.copyOf(coordinates);
    }

    public void addCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public void deleteCharAtCoordinates(Table2 table) {
        for (Coordinate item : coordinates) {
            table.deleteCoordinate(item.getX(), item.getY());
        }
        coordinates = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WordStartingCoordinate that = (WordStartingCoordinate) o;
        return alignment == that.alignment;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), alignment);
    }

    @Override
        public String toString() {
            String s = "";
            if (alignment == Alignment.HORISONTAL) {
                s += "[H]";
            }
            else s += "[V]";
            return s += super.toString();
        }
    }
