package wordgame.chris.jpa;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class Table2Test {

    private Table2 table2;
    private WordGameJpaDAO wordGameJpaDAO;

    @BeforeEach
    void init() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("wordgame-jpa");
        wordGameJpaDAO = new WordGameJpaDAO(entityManagerFactory);
        MariaDbDataSource mDs = MDataSource.getMariaDbDataSource();
        table2 = new Table2(new Random(1));
        Flyway flyway = Flyway.configure().dataSource(mDs).load();
        flyway.clean();
        flyway.migrate();
        wordGameJpaDAO.addWordsSeperatedBy("src/main/resources/szövegek/szavak.txt", "\n");
        wordGameJpaDAO.addWordsSeperatedBy("src/main/resources/szövegek/szavak2.txt", " ");
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
        String likePattern = table2.likePatternMaker(new WordStartingCoordinate(0,1, Alignment.HORISONTAL));
        assertEquals("_", likePattern);

    }

    @Test
    void wordLengthFromStartingCoordTest() {
        int wordLength = table2.wordLengthFromStartingCoordinate(new WordStartingCoordinate(1,0,  Alignment.HORISONTAL));
        assertEquals(2, wordLength);
        wordLength = table2.wordLengthFromStartingCoordinate(new WordStartingCoordinate(13,0, Alignment.HORISONTAL));
        assertEquals(2, wordLength);
        wordLength = table2.wordLengthFromStartingCoordinate(new WordStartingCoordinate(0,1, Alignment.VERTICAL));
        assertEquals(8, wordLength);
        wordLength = table2.wordLengthFromStartingCoordinate(new WordStartingCoordinate(12,13, Alignment.VERTICAL));
        assertEquals(2, wordLength);


    }

    @Test
    void iswordWritableTest() {
        assertTrue(table2.isWordWritable("HU", new WordStartingCoordinate(1,0, Alignment.HORISONTAL)));
        assertFalse(table2.isWordWritable("DANIELLA", new WordStartingCoordinate(1,0, Alignment.VERTICAL)));
        assertFalse(table2.isWordWritable("HU", new WordStartingCoordinate(0,1, Alignment.HORISONTAL)));
    }


    @Test
    void wordWriterTest() {
        table2.fillWordFromCoordinate("YK", new WordStartingCoordinate(0,1, Alignment.VERTICAL));
        table2.fillWordFromCoordinate("US", new WordStartingCoordinate(13,0, Alignment.HORISONTAL));
        table2.printTable();
        assertEquals("Y", table2.readCharacterAtCoordinate(new WordStartingCoordinate(0,1, Alignment.VERTICAL)));
        assertEquals("K", table2.readCharacterAtCoordinate(new WordStartingCoordinate(0,2, Alignment.VERTICAL)));
        assertEquals("U", table2.readCharacterAtCoordinate(new WordStartingCoordinate(13,0, Alignment.HORISONTAL)));
        assertEquals("S", table2.readCharacterAtCoordinate(new WordStartingCoordinate(14,0, Alignment.HORISONTAL)));
    }

    @Test
    void readCharAtCoordTest() {
        assertEquals(Table2.BLACK_GRID_PLACEHOLDER, table2.readCharacterAtCoordinate(new WordStartingCoordinate(0,0, Alignment.VERTICAL)));
        assertEquals(Table2.EMPTY_GRID_PLACEHOLDER, table2.readCharacterAtCoordinate(new WordStartingCoordinate(0,1, Alignment.VERTICAL)));

    }

    @Test
    void insertCharacterTest() {
        table2.insertCharacter("Y", new WordStartingCoordinate(1, 0, Alignment.HORISONTAL));
        assertEquals("Y", table2.readCharacterAtCoordinate(new WordStartingCoordinate(1, 0, Alignment.HORISONTAL)));
        table2.insertCharacter("Y", new WordStartingCoordinate(1, 0, Alignment.HORISONTAL));
        assertThrows(IllegalStateException.class, () -> {table2.insertCharacter("X", new WordStartingCoordinate(1, 0, Alignment.HORISONTAL));   });
        assertThrows(IllegalStateException.class, () -> {table2.insertCharacter("X", new WordStartingCoordinate(0, 0, Alignment.HORISONTAL));   });
    }

    @Test
    void getWordTest() {
        table2.fillWordFromCoordinate("HU", new WordStartingCoordinate(1,0, Alignment.HORISONTAL));
        assertEquals("HU", table2.getWordFromStartingCoordinate(new WordStartingCoordinate(1,0, Alignment.HORISONTAL)));
        table2.fillWordFromCoordinate("DANIELLA", new WordStartingCoordinate(0,1, Alignment.VERTICAL));
        assertEquals("DANIELLA", table2.getWordFromStartingCoordinate(new WordStartingCoordinate(0,1, Alignment.VERTICAL)));
    }

}
