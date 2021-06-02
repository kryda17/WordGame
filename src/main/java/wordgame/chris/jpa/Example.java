package wordgame.chris.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Example {

   private Map<WordStartingCoordinate, TriedWordsAndUsedCharsOnStartingCoordinate> search = new HashMap<>();

    public Example(List<WordStartingCoordinate> coordinates) {
        for (WordStartingCoordinate coordinate : coordinates) {
            search.put(coordinate, new TriedWordsAndUsedCharsOnStartingCoordinate());
        }
    }

    public void addWord(String word, WordStartingCoordinate coordinate) {
        search.get(coordinate).addWord(word);
    }

    public void clearWords(WordStartingCoordinate coordinate) {
        search.get(coordinate).clearWords();
    }

    public List<String> getWords(WordStartingCoordinate coordinate) {
        return List.copyOf(search.get(coordinate).getWords());
    }

    public List<Coordinate> getCoordinates(Coordinate coordinate) {
        return List.copyOf(search.get(coordinate).getCoordinates());
    }

    public void addCoordinates(List<Coordinate> coordinates, Coordinate coordinate) {
        search.get(coordinate).addCoordinates(coordinates);
    }

    public void deleteCharAtCoordinates(Table2 table2, Coordinate coordinate) {
        for (Coordinate item : search.get(coordinate).getCoordinates()) {
            table2.deleteCoordinate(item);
        }
    }

    public TriedWordsAndUsedCharsOnStartingCoordinate get(WordStartingCoordinate coordinate) {
        return search.get(coordinate);
    }
}
