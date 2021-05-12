package wordgame.chris;

import wordgame.Coordinate;

import java.util.List;

public class WordLengthFromCoordinate {

    private wordgame.Coordinate coordinate;
    private int wordLength;

    public WordLengthFromCoordinate(wordgame.Coordinate coordinate, int wordLength) {
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
