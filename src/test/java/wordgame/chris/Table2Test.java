package wordgame.chris;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wordgame.Coordinate;
import wordgame.WordGameDAO;
import wordgame.chris.Alignment;
import wordgame.chris.Table2;
import wordgame.chris.VerticalWordsGenerator;

class Table2Test {

    private wordgame.WordGameDAO wordGameDAO;

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
        table.fillWordFromCoordinate("TE", new wordgame.Coordinate(0,1), Alignment.HORISONTAL);
        table.printTable();
        table.fillWordFromCoordinate("MI", new Coordinate(1,0), Alignment.VERTICAL);
        table.printTable();
        table.requiredHorWordsLength();
        table.requiredVerticalWordsLength();
    }


}
