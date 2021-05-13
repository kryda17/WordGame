package wordgame.chris;

import java.util.List;

public class VerAndHorWordsLengthsFromCoordinates {

    private List<WordLengthFromCoordinate> wordLengthFromCoordinatesHorisontal;
    private List<WordLengthFromCoordinate> wordLengthFromCoordinatesVertical;

    public VerAndHorWordsLengthsFromCoordinates(List<WordLengthFromCoordinate> wordLengthFromCoordinatesHorisontal, List<WordLengthFromCoordinate> wordLengthFromCoordinatesVertical) {
        this.wordLengthFromCoordinatesHorisontal = wordLengthFromCoordinatesHorisontal;
        this.wordLengthFromCoordinatesVertical = wordLengthFromCoordinatesVertical;
    }

    public List<WordLengthFromCoordinate> getWordLengthFromCoordinatesHorisontal() {
        return List.copyOf(wordLengthFromCoordinatesHorisontal);
    }

    public List<WordLengthFromCoordinate> getWordLengthFromCoordinatesVertical() {
        return List.copyOf(wordLengthFromCoordinatesVertical);
    }
}
