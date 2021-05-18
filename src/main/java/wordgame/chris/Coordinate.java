package wordgame.chris;

import java.util.ArrayList;
import java.util.List;

public class Coordinate {

    private int x;
    private int y;
    private Alignment alignment;
    private List<String> words = new ArrayList<>();

    public Coordinate(int x, int y, Alignment alignment) {
        this.x = x;
        this.y = y;
        this.alignment = alignment;
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
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
        return words;
    }

}
