package wordgame.chris;

public class WordLengthFromCoordinate {

    private Coordinate coordinate;
    private int wordLength;
    private Alignment alignment;

    public WordLengthFromCoordinate(Coordinate coordinate, int wordLength, Alignment alignment) {
        this.coordinate = coordinate;
        this.wordLength = wordLength;
        this.alignment = alignment;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public int getWordLength() {
        return wordLength;
    }

    public Alignment getAlignment() {
        return alignment;
    }
}
