package wordgame.chris;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class VerAndHorWordsLengthsFromCoordinates {

    private List<Coordinate> coordinates;

    public VerAndHorWordsLengthsFromCoordinates(List<Coordinate> coordinatesHorisontal, List<Coordinate> coordinatesVertical) {
        this.coordinates = coordinatesHorisontal;
        this.coordinates.addAll(coordinatesVertical);
        this.coordinates.sort(Comparator.comparingInt(Coordinate::getY).thenComparing(Coordinate::getX));
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public List<WordLengthFromCoordinate> howMany(int loopNum, List<WordLengthFromCoordinate> wordLengthFromCoordinates) {
        List<WordLengthFromCoordinate> lines = new ArrayList<>();
        for (WordLengthFromCoordinate item : wordLengthFromCoordinates) {
            if (item.getCoordinate().getX() == loopNum) {
                lines.add(item);
            }
        }
        return lines;
    }
}
