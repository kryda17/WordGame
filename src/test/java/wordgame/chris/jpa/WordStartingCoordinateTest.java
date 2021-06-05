package wordgame.chris.jpa;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class WordStartingCoordinateTest {

    @Test
    void toStringTest() {
        assertEquals("[H]X:0,Y:1", new WordStartingCoordinate(0,1,Alignment.HORISONTAL).toString());
        assertEquals("[V]X:1,Y:0", new WordStartingCoordinate(1,0,Alignment.VERTICAL).toString());
    }

    @Test
    void addWordTest() {
        WordStartingCoordinate wordStartingCoordinate = new WordStartingCoordinate(0,1, Alignment.HORISONTAL);
        wordStartingCoordinate.addWord("John Wick");
        assertEquals("John Wick" ,wordStartingCoordinate.getWords().get(0));
        assertEquals(1, wordStartingCoordinate.getWords().size());
    }

    @Test
    void clearWordsTest() {
        WordStartingCoordinate wordStartingCoordinate = new WordStartingCoordinate(0,1, Alignment.HORISONTAL);
        wordStartingCoordinate.addWord("John Wick");
        wordStartingCoordinate.addWord("John Doe");
        assertEquals(2, wordStartingCoordinate.getWords().size());
        wordStartingCoordinate.clearWords();
        assertEquals(0, wordStartingCoordinate.getWords().size());
    }

    @Test
    void addCoordinatesTest() {
        WordStartingCoordinate wordStartingCoordinate = new WordStartingCoordinate(0,1, Alignment.HORISONTAL);
        wordStartingCoordinate.addCoordinates(List.of(new Coordinate(1,0), new Coordinate(0,1)));
        assertEquals(2, wordStartingCoordinate.getCoordinates().size());
    }

    @Test
    void clearCoordinatesTest() {
        WordStartingCoordinate wordStartingCoordinate = new WordStartingCoordinate(0,1, Alignment.HORISONTAL);
        wordStartingCoordinate.addCoordinates(List.of(new Coordinate(1,0), new Coordinate(0,1)));
        wordStartingCoordinate.rollbackWord(new Table2(15,new Random(1)));
        assertEquals(0, wordStartingCoordinate.getCoordinates().size());
    }

    @Test
    void equalsTest() {
        WordStartingCoordinate wordStartingCoordinate = new WordStartingCoordinate(0,1, Alignment.HORISONTAL);
        WordStartingCoordinate wordStartingCoordinateAnother = new WordStartingCoordinate(0,1, Alignment.HORISONTAL);
        assertTrue(wordStartingCoordinate.equals(wordStartingCoordinateAnother));
        assertFalse(wordStartingCoordinate.equals(new WordStartingCoordinate(0,1, Alignment.VERTICAL)));
        assertFalse(wordStartingCoordinate.equals(new WordStartingCoordinate(1,0, Alignment.VERTICAL)));
    }
}