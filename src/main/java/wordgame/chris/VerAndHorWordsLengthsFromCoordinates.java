package wordgame.chris;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class VerAndHorWordsLengthsFromCoordinates {

    private List<WordLengthFromCoordinate> wordLengthFromCoordinatesHorisontal;
    private List<WordLengthFromCoordinate> wordLengthFromCoordinatesVertical;

    public VerAndHorWordsLengthsFromCoordinates(List<WordLengthFromCoordinate> wordLengthFromCoordinatesHorisontal, List<WordLengthFromCoordinate> wordLengthFromCoordinatesVertical) {
        wordLengthFromCoordinatesHorisontal.sort(Comparator.comparingInt(o -> o.getCoordinate().getxCoord()));
        this.wordLengthFromCoordinatesHorisontal = wordLengthFromCoordinatesHorisontal;
        wordLengthFromCoordinatesVertical.sort(Comparator.comparingInt(o -> o.getCoordinate().getyCoord()));
        this.wordLengthFromCoordinatesVertical = wordLengthFromCoordinatesVertical;
    }

    public List<WordLengthFromCoordinate> getWordLengthFromCoordinatesHorisontal() {
        return List.copyOf(wordLengthFromCoordinatesHorisontal);
    }

    public List<WordLengthFromCoordinate> getWordLengthFromCoordinatesVertical() {
        return List.copyOf(wordLengthFromCoordinatesVertical);
    }

    public List<WordLengthFromCoordinate> howMany(int loopNum, List<WordLengthFromCoordinate> wordLengthFromCoordinates) {
        List<WordLengthFromCoordinate> lines = new ArrayList<>();
        for (WordLengthFromCoordinate item : wordLengthFromCoordinates) {
            if (item.getCoordinate().getxCoord() == loopNum) {
                lines.add(item);
            }
        }
        return lines;
    }
}
