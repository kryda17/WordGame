package wordgame.chris.jdbc;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;
import wordgame.chris.jpa.Alignment;
import wordgame.chris.jpa.Coordinate;
import wordgame.chris.jpa.MDataSource;
import wordgame.chris.jpa.Table2;
import wordgame.chris.jpa.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class Table2Test {

    private wordgame.chris.jpa.Table2 table2;
    private WordGameJdbcDAO wordGameJdbcDAO;

    @BeforeEach
    void init() {
        wordGameJdbcDAO = new WordGameJdbcDAO();
        MariaDbDataSource mDs = MDataSource.getMariaDbDataSource();
        table2 = new wordgame.chris.jpa.Table2(new Random(1));
        Flyway flyway = Flyway.configure().dataSource(mDs).load();
        flyway.clean();
        flyway.migrate();
        wordGameJdbcDAO.addWordsSeperatedBy("src/main/resources/szövegek/szavak.txt", "\n");
        wordGameJdbcDAO.addWordsSeperatedBy("src/main/resources/szövegek/szavak2.txt", " ");
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
        String likePattern = table2.likePatternMaker(new wordgame.chris.jpa.Coordinate(0,1, wordgame.chris.jpa.Alignment.HORISONTAL));
        assertEquals("_", likePattern);

    }

    @Test
    void wordLengthFromStartingCoordTest() {
        int wordLength = table2.wordLengthFromStartingCoordinate(new wordgame.chris.jpa.Coordinate(1,0,  wordgame.chris.jpa.Alignment.HORISONTAL));
        assertEquals(2, wordLength);
        wordLength = table2.wordLengthFromStartingCoordinate(new wordgame.chris.jpa.Coordinate(13,0, wordgame.chris.jpa.Alignment.HORISONTAL));
        assertEquals(2, wordLength);
        wordLength = table2.wordLengthFromStartingCoordinate(new wordgame.chris.jpa.Coordinate(0,1, wordgame.chris.jpa.Alignment.VERTICAL));
        assertEquals(8, wordLength);
        wordLength = table2.wordLengthFromStartingCoordinate(new wordgame.chris.jpa.Coordinate(12,13, wordgame.chris.jpa.Alignment.VERTICAL));
        assertEquals(2, wordLength);


    }

    @Test
    void iswordWritableTest() {
        assertTrue(table2.isWordWritable("HU", new wordgame.chris.jpa.Coordinate(1,0, wordgame.chris.jpa.Alignment.HORISONTAL)));
        assertFalse(table2.isWordWritable("DANIELLA", new wordgame.chris.jpa.Coordinate(1,0, wordgame.chris.jpa.Alignment.VERTICAL)));
        assertFalse(table2.isWordWritable("HU", new wordgame.chris.jpa.Coordinate(0,1, wordgame.chris.jpa.Alignment.HORISONTAL)));
    }


    @Test
    void wordWriterTest() {
        table2.fillWordFromCoordinate("YK", new wordgame.chris.jpa.Coordinate(0,1, wordgame.chris.jpa.Alignment.VERTICAL));
        table2.fillWordFromCoordinate("US", new wordgame.chris.jpa.Coordinate(13,0, wordgame.chris.jpa.Alignment.HORISONTAL));
        table2.printTable();
        assertEquals("Y", table2.readCharacterAtCoordinate(new wordgame.chris.jpa.Coordinate(0,1, wordgame.chris.jpa.Alignment.VERTICAL)));
        assertEquals("K", table2.readCharacterAtCoordinate(new wordgame.chris.jpa.Coordinate(0,2, wordgame.chris.jpa.Alignment.VERTICAL)));
        assertEquals("U", table2.readCharacterAtCoordinate(new wordgame.chris.jpa.Coordinate(13,0, wordgame.chris.jpa.Alignment.HORISONTAL)));
        assertEquals("S", table2.readCharacterAtCoordinate(new wordgame.chris.jpa.Coordinate(14,0, wordgame.chris.jpa.Alignment.HORISONTAL)));
    }

    @Test
    void readCharAtCoordTest() {
        assertEquals(wordgame.chris.jpa.Table2.BLACK_GRID_PLACEHOLDER, table2.readCharacterAtCoordinate(new wordgame.chris.jpa.Coordinate(0,0, wordgame.chris.jpa.Alignment.VERTICAL)));
        assertEquals(Table2.EMPTY_GRID_PLACEHOLDER, table2.readCharacterAtCoordinate(new wordgame.chris.jpa.Coordinate(0,1, wordgame.chris.jpa.Alignment.VERTICAL)));

    }

    @Test
    void insertCharacterTest() {
        table2.insertCharacter("Y", new wordgame.chris.jpa.Coordinate(1, 0, wordgame.chris.jpa.Alignment.HORISONTAL));
        assertEquals("Y", table2.readCharacterAtCoordinate(new wordgame.chris.jpa.Coordinate(1, 0, wordgame.chris.jpa.Alignment.HORISONTAL)));
        table2.insertCharacter("Y", new wordgame.chris.jpa.Coordinate(1, 0, wordgame.chris.jpa.Alignment.HORISONTAL));
        assertThrows(IllegalStateException.class, () -> {table2.insertCharacter("X", new wordgame.chris.jpa.Coordinate(1, 0, wordgame.chris.jpa.Alignment.HORISONTAL));   });
        assertThrows(IllegalStateException.class, () -> {table2.insertCharacter("X", new Coordinate(0, 0, Alignment.HORISONTAL));   });
    }

}
