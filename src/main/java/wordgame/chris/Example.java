package wordgame.chris;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Example {

   private Map<Coordinate, TriedWordsAndUsedCharsOnStartingCoordinate> search = new HashMap<>();

    public Example(List<Coordinate> coordinates) {
        for (Coordinate coordinate : coordinates) {
            search.put(coordinate, new TriedWordsAndUsedCharsOnStartingCoordinate());
        }
    }

    public TriedWordsAndUsedCharsOnStartingCoordinate get(Coordinate coordinate) {
        return search.get(coordinate);
    }
}
