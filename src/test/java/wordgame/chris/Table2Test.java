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
        VerticalWordsGenerator v = new VerticalWordsGenerator();
        v.fillDBFromHorisontalWords(table2.getTable());
        wordGameDAO.addWordsSeperatedBySpaceFromFile("src/main/resources/szövegek/szavak.txt", " ");
        wordGameDAO.addWordsSeperatedBySpaceFromFile("src/main/resources/szövegek/szavak2.txt", "\n");
    }

    @Test
    void testPrint() {
        table2.printTable();
    }

    @Test
    void likePatternMakerTest() {
        String lkePattern = table2.likePatternMaker(new Coordinate(0,1), Alignment.VERTICAL);
        assertEquals("YK", lkePattern);
    }

    @Test
    void wordLengthFromStartingCoordTest() {
        int wordLength = table2.wordLengthFromStartingCoordinate(new Coordinate(1,0), Alignment.HORISONTAL);
        assertEquals(8, wordLength);
        wordLength = table2.wordLengthFromStartingCoordinate(new Coordinate(13,0), Alignment.HORISONTAL);
        assertEquals(2, wordLength);
        wordLength = table2.wordLengthFromStartingCoordinate(new Coordinate(0,1), Alignment.VERTICAL);
        assertEquals(2, wordLength);
        wordLength = table2.wordLengthFromStartingCoordinate(new Coordinate(13,0), Alignment.HORISONTAL);
        assertEquals(2, wordLength);


    }

    @Test
    void wordWriterTest() {
        table2.fillWordFromCoordinate("YK", new Coordinate(0,1), Alignment.VERTICAL);
        table2.fillWordFromCoordinate("US", new Coordinate(13,0), Alignment.HORISONTAL);
        table2.printTable();
        assertEquals("Y", table2.readStringAtCoordinate(new Coordinate(0,1)));
        assertEquals("K", table2.readStringAtCoordinate(new Coordinate(0,2)));
        assertEquals("U", table2.readStringAtCoordinate(new Coordinate(13,0)));
        assertEquals("S", table2.readStringAtCoordinate(new Coordinate(14,0)));
    }

}
