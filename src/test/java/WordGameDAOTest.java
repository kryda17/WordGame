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
    void testCreateAndQuery() {
        List<String> strings = new ArrayList<>();
        wordGameDAO.createWord("John Doe");
        assertEquals(Arrays.asList("John Doe"), wordGameDAO.queryWords());
    }

}