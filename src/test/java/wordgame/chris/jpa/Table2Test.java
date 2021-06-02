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

    private FillTable fillTable;
    private WordGameJpaDAO wordGameJpaDAO;

    @BeforeEach
    void init() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("wordgame-jpa");
        wordGameJpaDAO = new WordGameJpaDAO(entityManagerFactory);
        MariaDbDataSource mDs = MDataSource.getMariaDbDataSource();
        fillTable = new FillTable(new Table2(15 ,new Random(1)));
        Flyway flyway = Flyway.configure().dataSource(mDs).load();
        flyway.clean();
        flyway.migrate();
        wordGameJpaDAO.addWordsSeperatedBy("src/main/resources/szövegek/szavak.txt", "\n");
        wordGameJpaDAO.addWordsSeperatedBy("src/main/resources/szövegek/szavak2.txt", " ");
    }

    @Test
    void testFunc() {
        fillTable.func(fillTable.getTable());
        fillTable.printTable();
    }

    @Test
    void testPrint() {
        fillTable.printTable();
    }

    @Test
    void likePatternMakerTest() {
        String likePattern = fillTable.likePatternMaker(new WordStartingCoordinate(0,1, Alignment.HORISONTAL));
        assertEquals("_", likePattern);

    }

    @Test
    void wordLengthFromStartingCoordTest() {
        int wordLength = fillTable.wordLengthFromStartingCoordinate(new WordStartingCoordinate(1,0,  Alignment.HORISONTAL));
        assertEquals(2, wordLength);
        wordLength = fillTable.wordLengthFromStartingCoordinate(new WordStartingCoordinate(13,0, Alignment.HORISONTAL));
        assertEquals(2, wordLength);
        wordLength = fillTable.wordLengthFromStartingCoordinate(new WordStartingCoordinate(0,1, Alignment.VERTICAL));
        assertEquals(8, wordLength);
        wordLength = fillTable.wordLengthFromStartingCoordinate(new WordStartingCoordinate(12,13, Alignment.VERTICAL));
        assertEquals(2, wordLength);


    }

    @Test
    void iswordWritableTest() {
        assertTrue(fillTable.isWordWritable("HU", new WordStartingCoordinate(1,0, Alignment.HORISONTAL)));
        assertFalse(fillTable.isWordWritable("DANIELLA", new WordStartingCoordinate(1,0, Alignment.VERTICAL)));
        assertFalse(fillTable.isWordWritable("HU", new WordStartingCoordinate(0,1, Alignment.HORISONTAL)));
    }


    @Test
    void wordWriterTest() {
        fillTable.fillWordFromCoordinate("YK", new WordStartingCoordinate(0,1, Alignment.VERTICAL));
        fillTable.fillWordFromCoordinate("US", new WordStartingCoordinate(13,0, Alignment.HORISONTAL));
        fillTable.printTable();
        assertEquals("Y", fillTable.readCharacterAtCoordinate(new WordStartingCoordinate(0,1, Alignment.VERTICAL)));
        assertEquals("K", fillTable.readCharacterAtCoordinate(new WordStartingCoordinate(0,2, Alignment.VERTICAL)));
        assertEquals("U", fillTable.readCharacterAtCoordinate(new WordStartingCoordinate(13,0, Alignment.HORISONTAL)));
        assertEquals("S", fillTable.readCharacterAtCoordinate(new WordStartingCoordinate(14,0, Alignment.HORISONTAL)));
    }

    @Test
    void readCharAtCoordTest() {
        assertEquals(fillTable.getTable().BLACK_GRID_PLACEHOLDER, fillTable.readCharacterAtCoordinate(new WordStartingCoordinate(0,0, Alignment.VERTICAL)));
        assertEquals(fillTable.getTable().EMPTY_GRID_PLACEHOLDER, fillTable.readCharacterAtCoordinate(new WordStartingCoordinate(0,1, Alignment.VERTICAL)));

    }

    @Test
    void insertCharacterTest() {
        fillTable.insertCharacter("Y", new WordStartingCoordinate(1, 0, Alignment.HORISONTAL));
        assertEquals("Y", fillTable.readCharacterAtCoordinate(new WordStartingCoordinate(1, 0, Alignment.HORISONTAL)));
        fillTable.insertCharacter("Y", new WordStartingCoordinate(1, 0, Alignment.HORISONTAL));
        assertThrows(IllegalStateException.class, () -> {fillTable.insertCharacter("X", new WordStartingCoordinate(1, 0, Alignment.HORISONTAL));   });
        assertThrows(IllegalStateException.class, () -> {fillTable.insertCharacter("X", new WordStartingCoordinate(0, 0, Alignment.HORISONTAL));   });
    }

    @Test
    void getWordTest() {
        fillTable.fillWordFromCoordinate("HU", new WordStartingCoordinate(1,0, Alignment.HORISONTAL));
        assertEquals("HU", fillTable.getWordFromStartingCoordinate(new WordStartingCoordinate(1,0, Alignment.HORISONTAL)));
        fillTable.fillWordFromCoordinate("DANIELLA", new WordStartingCoordinate(0,1, Alignment.VERTICAL));
        assertEquals("DANIELLA", fillTable.getWordFromStartingCoordinate(new WordStartingCoordinate(0,1, Alignment.VERTICAL)));
    }

}
