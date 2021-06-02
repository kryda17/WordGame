package wordgame.chris.jdbc;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordGameJdbcDAOTest {

    private WordGameJdbcDAO wordGameJdbcDAO;

    @BeforeEach
    void init() {
        wordGameJdbcDAO = new WordGameJdbcDAO();
        Flyway flyway = Flyway.configure().dataSource(wordGameJdbcDAO.getDs()).load();
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void testAddWord() {
        wordGameJdbcDAO.addWords("John");
        assertEquals(Arrays.asList("JOHN"), wordGameJdbcDAO.queryWordsWithLenght(4));
    }

    @Test
    void testAddWordsWithVarargs() {
        wordGameJdbcDAO.addWords("JOHN" , "JACK");
        assertEquals(Arrays.asList("JOHN", "JACK"), wordGameJdbcDAO.queryWordsWithLenght(4));

        wordGameJdbcDAO.addWords(new String[] {"Jane", "Wick"});
        assertEquals(Arrays.asList("JOHN", "JACK", "JANE", "WICK"), wordGameJdbcDAO.queryWordsWithLenght(4));
    }

    @Test
    void testReadFromFile() {
        wordGameJdbcDAO.addWordsSeperatedBy("src/main/resources//szövegek/testfile.txt", " ");
        assertEquals(Arrays.asList("TEST"), wordGameJdbcDAO.queryWordsWithLenght(4));
    }

    @Test
    void testLike() {
        wordGameJdbcDAO.addWords("John", "Jack");
        List<String> words = wordGameJdbcDAO.queryWordsWithLenghtAndLike(4, "Jo__");
        assertEquals(1, words.size());
        assertEquals("JOHN", words.get(0));
    }

}