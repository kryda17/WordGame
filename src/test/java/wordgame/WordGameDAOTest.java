package wordgame;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class WordGameDAOTest {

    private WordGameDAO wordGameDAO;

    @BeforeEach
    void init() {
        wordGameDAO = new WordGameDAO();
        Flyway flyway = Flyway.configure().dataSource(wordGameDAO.getDs()).load();
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void testAddWord() {
        wordGameDAO.addWords("John Doe");
        assertEquals(Arrays.asList("John Doe"), wordGameDAO.queryWords());
    }

    @Test
    void testAddWithVarargs() {
        wordGameDAO.addWords("John Doe", "Jack Doe");
        assertEquals(Arrays.asList("John Doe", "Jack Doe"), wordGameDAO.queryWords());
    }

    @Test
    void testReadFromFile() {
        wordGameDAO.addWordsSeperatedBySpaceFromFile("src/main/resources/testfile.txt");
        assertEquals(Arrays.asList("Test", "test2"), wordGameDAO.queryWords());
    }

}