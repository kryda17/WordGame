package wordgame.chris;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wordgame.WordGameDAO;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordGameDAOTest {

    private wordgame.WordGameDAO wordGameDAO;

    @BeforeEach
    void init() {
        wordGameDAO = new WordGameDAO();
        Flyway flyway = Flyway.configure().dataSource(wordGameDAO.getDs()).load();
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void testAddWord() {
        wordGameDAO.addWords("John");
        assertEquals(Arrays.asList("John"), wordGameDAO.queryWordsWithLenght(4));
    }

    @Test
    void testAddWordsWithVarargs() {
        wordGameDAO.addWords("John", "Jack");
        assertEquals(Arrays.asList("John", "Jack"), wordGameDAO.queryWordsWithLenght(4));

        wordGameDAO.addWords(new String[] {"Jane", "Wick"});
        assertEquals(Arrays.asList("John", "Jack", "Jane", "Wick"), wordGameDAO.queryWordsWithLenght(4));
    }

    @Test
    void testReadFromFile() {
        wordGameDAO.addWordsSeperatedBySpaceFromFile("src/main/resources//szövegek/testfile.txt", " ");
        assertEquals(Arrays.asList("Test"), wordGameDAO.queryWordsWithLenght(4));
    }

}