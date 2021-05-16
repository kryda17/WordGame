package wordgame.chris;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wordgame.chris.helpers.VerticalWordsGenerator;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Table2Test {

    private Table2 table2 = new Table2(new Random(1));
    private WordGameDAO wordGameDAO;

    @BeforeEach
    void init() {
        wordGameDAO = new WordGameDAO();
        Flyway flyway = Flyway.configure().dataSource(wordGameDAO.getDs()).load();
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void testPrint() {

        table2.printTable();

        VerticalWordsGenerator v = new VerticalWordsGenerator();
        v.fillDBFromHorisontalWords(table2.getTable());

        wordGameDAO.addWordsSeperatedBySpaceFromFile("src/main/resources/szövegek/szavak.txt", " ");
        wordGameDAO.addWordsSeperatedBySpaceFromFile("src/main/resources/szövegek/szavak2.txt", "\n");

    }

    @Test
    void likeMakerTest() {
        String lkePattern = table2.likePatternMaker(new WordLengthFromCoordinate(new Coordinate(0,1), 2, Alignment.VERTICAL));
        assertEquals("__", lkePattern);
    }

    @Test
    void wordLengthFromStartingCoordTest() {
        int wordLength = table2.wordLengthFromStartingCoordinate(new Coordinate(1,0), Alignment.HORISONTAL);
        assertEquals(8, wordLength);
        wordLength = table2.wordLengthFromStartingCoordinate(new Coordinate(13,0), Alignment.HORISONTAL);
        assertEquals(2, wordLength);


    }

    @Test
    void wordWriterTest() {
        table2.fillWordFromCoordinate("TE", new Coordinate(0,1), Alignment.VERTICAL);
        table2.fillWordFromCoordinate("MI", new Coordinate(13,0), Alignment.HORISONTAL);
        table2.printTable();
        assertEquals("T", table2.readCharAtCoordinate(new Coordinate(0,1)));
        assertEquals("E", table2.readCharAtCoordinate(new Coordinate(0,2)));
        assertEquals("M", table2.readCharAtCoordinate(new Coordinate(13,0)));
        assertEquals("I", table2.readCharAtCoordinate(new Coordinate(14,0)));
    }

}
