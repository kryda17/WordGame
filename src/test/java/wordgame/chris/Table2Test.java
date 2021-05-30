package wordgame.chris;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class Table2Test {

    private Table2 table2;
    private WordGameDAO wordGameDAO;


    @BeforeEach
    void init() {
        table2 = new Table2(new Random(1));
        wordGameDAO = new WordGameDAO();
        Flyway flyway = Flyway.configure().dataSource(wordGameDAO.getDs()).load();
        flyway.clean();
        flyway.migrate();
        wordGameDAO.addWordsSeperatedBy("src/main/resources/szövegek/szavak.txt", "\n");
        wordGameDAO.addWordsSeperatedBy("src/main/resources/szövegek/szavak2.txt", " ");


    }

    @Test
    void testFunc() {
        table2.func();
        table2.printTable();
    }

    @Test
    void testPrint() {
        table2.printTable();
    }

    @Test
    void likePatternMakerTest() {
        String likePattern = table2.likePatternMaker(new Coordinate(0,1, Alignment.HORISONTAL));
        assertEquals("_", likePattern);

    }

    @Test
    void wordLengthFromStartingCoordTest() {
        int wordLength = table2.wordLengthFromStartingCoordinate(new Coordinate(1,0,  Alignment.HORISONTAL));
        assertEquals(2, wordLength);
        wordLength = table2.wordLengthFromStartingCoordinate(new Coordinate(13,0, Alignment.HORISONTAL));
        assertEquals(2, wordLength);
        wordLength = table2.wordLengthFromStartingCoordinate(new Coordinate(0,1, Alignment.VERTICAL));
        assertEquals(8, wordLength);
        wordLength = table2.wordLengthFromStartingCoordinate(new Coordinate(12,13, Alignment.VERTICAL));
        assertEquals(2, wordLength);


    }

    @Test
    void iswordWritableTest() {
        assertTrue(table2.isWordWritable("HU", new Coordinate(1,0, Alignment.HORISONTAL)));
        assertFalse(table2.isWordWritable("DANIELLA", new Coordinate(1,0, Alignment.VERTICAL)));
        assertFalse(table2.isWordWritable("HU", new Coordinate(0,1, Alignment.HORISONTAL)));
    }


    @Test
    void wordWriterTest() {
        table2.fillWordFromCoordinate("YK", new Coordinate(0,1, Alignment.VERTICAL));
        table2.fillWordFromCoordinate("US", new Coordinate(13,0, Alignment.HORISONTAL));
        table2.printTable();
        assertEquals("Y", table2.readCharacterAtCoordinate(new Coordinate(0,1, Alignment.VERTICAL)));
        assertEquals("K", table2.readCharacterAtCoordinate(new Coordinate(0,2, Alignment.VERTICAL)));
        assertEquals("U", table2.readCharacterAtCoordinate(new Coordinate(13,0, Alignment.HORISONTAL)));
        assertEquals("S", table2.readCharacterAtCoordinate(new Coordinate(14,0, Alignment.HORISONTAL)));
    }

    @Test
    void readCharAtCoordTest() {
        assertEquals(Table2.BLACK_GRID_PLACEHOLDER, table2.readCharacterAtCoordinate(new Coordinate(0,0, Alignment.VERTICAL)));
        assertEquals(Table2.EMPTY_GRID_PLACEHOLDER, table2.readCharacterAtCoordinate(new Coordinate(0,1, Alignment.VERTICAL)));

    }

    @Test
    void insertCharacterTest() {
        table2.insertCharacter("Y", new Coordinate(1, 0, Alignment.HORISONTAL));
        assertEquals("Y", table2.readCharacterAtCoordinate(new Coordinate(1, 0, Alignment.HORISONTAL)));
        table2.insertCharacter("Y", new Coordinate(1, 0, Alignment.HORISONTAL));
        assertThrows(IllegalStateException.class, () -> {table2.insertCharacter("X", new Coordinate(1, 0, Alignment.HORISONTAL));   });
        assertThrows(IllegalStateException.class, () -> {table2.insertCharacter("X", new Coordinate(0, 0, Alignment.HORISONTAL));   });
    }

}
