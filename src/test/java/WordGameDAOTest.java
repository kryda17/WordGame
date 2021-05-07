import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        List<String> strings = new ArrayList<>();
        wordGameDAO.addWords("John Doe");
        assertEquals(Arrays.asList("John Doe"), wordGameDAO.queryWords());
    }

    @Test
    void testAddWithVarargs() {
        wordGameDAO.addWords("John Doe", "Jack Doe");
        assertEquals(Arrays.asList("John Doe", "Jack Doe"), wordGameDAO.queryWords());
    }

}