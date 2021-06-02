package wordgame.chris.jpa;

import java.util.ArrayList;
import java.util.List;

public class TriedWordsAndUsedCharsOnStartingCoordinate {

    private List<String> words = new ArrayList<>();
    List<Coordinate> coordinates = new ArrayList<>();

    public void addWord(String word) {
        words.add(word);
    }

    public void clearWords() {
        words.clear();
    }

    public List<String> getWords() {
        return List.copyOf(words);
    }

    public List<Coordinate> getCoordinates() {
        return List.copyOf(coordinates);
    }

    public void addCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public void deleteCharAtCoordinates(Table2 table) {
        for (Coordinate item : coordinates) {
            table.deleteCoordinate(item);
        }
    }

}