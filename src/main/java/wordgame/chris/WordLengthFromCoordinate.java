package wordgame.chris;

public class WordLengthFromCoordinate {

    private Coordinate coordinate;
    private int wordLength;

    public WordLengthFromCoordinate(Coordinate coordinate, int wordLength) {
        this.coordinate = coordinate;
        this.wordLength = wordLength;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public int getWordLength() {
        return wordLength;
    }
}
