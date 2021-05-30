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

    public void addWord(String word, Coordinate coordinate) {
        search.get(coordinate).addWord(word);
    }

    public void clearWords(Coordinate coordinate) {
        search.get(coordinate).clearWords();
    }

    public List<String> getWords(Coordinate coordinate) {
        return List.copyOf(search.get(coordinate).getWords());
    }

    public List<Coordinate> getCoordinates(Coordinate coordinate) {
        return List.copyOf(search.get(coordinate).getCoordinates());
    }

    public void addCoordinates(List<Coordinate> coordinates, Coordinate coordinate) {
        search.get(coordinate).addCoordinates(coordinates);
    }

    public void deleteCharAtCoordinates(String[][] table, Coordinate coordinate) {
        for (Coordinate item : search.get(coordinate).getCoordinates()) {
            table[item.getY()][item.getX()] = Table2.EMPTY_GRID_PLACEHOLDER;
        }
    }

    public TriedWordsAndUsedCharsOnStartingCoordinate get(Coordinate coordinate) {
        return search.get(coordinate);
    }
}
