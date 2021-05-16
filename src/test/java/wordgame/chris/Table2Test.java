package wordgame.chris;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wordgame.chris.helpers.VerticalWordsGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Table2Test {

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
        Table2 table = new Table2();
        table.printTable();

        VerticalWordsGenerator v = new VerticalWordsGenerator();
        v.fillDBFromHorisontalWords(table.getTable());

        wordGameDAO.addWordsSeperatedBySpaceFromFile("src/main/resources/szövegek/szavak.txt", " ");
        wordGameDAO.addWordsSeperatedBySpaceFromFile("src/main/resources/szövegek/szavak2.txt", "\n");
        table.requiredHorWordsLengthAndStartingCoord();
        table.requiredVerticalWordsLengthAndStartingCoord();
    }

    @Test
    void likeMakerTest() {
        Table2 table2 = new Table2();
        String lkePattern = table2.likePatternMaker(new WordLengthFromCoordinate(new Coordinate(0,1), 2, Alignment.VERTICAL));
        assertEquals("__", lkePattern);
    }

    @Test
    void wordWriterTest() {
        Table2 table2 = new Table2();
        table2.fillWordFromCoordinate("TE", new Coordinate(0,1), Alignment.VERTICAL);
        table2.fillWordFromCoordinate("MI", new Coordinate(13,0), Alignment.HORISONTAL);
        table2.printTable();
        assertEquals("T", table2.readCharAtCoordinate(new Coordinate(0,1)));
        assertEquals("E", table2.readCharAtCoordinate(new Coordinate(0,2)));
        assertEquals("M", table2.readCharAtCoordinate(new Coordinate(13,0)));
        assertEquals("I", table2.readCharAtCoordinate(new Coordinate(14,0)));
    }

}
