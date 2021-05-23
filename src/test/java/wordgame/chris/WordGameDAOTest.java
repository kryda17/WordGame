package wordgame.chris;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        wordGameDAO.addWords("John");
        assertEquals(Arrays.asList("JOHN"), wordGameDAO.queryWordsWithLenght(4));
    }

    @Test
    void testAddWordsWithVarargs() {
        wordGameDAO.addWords("JOHN" , "JACK");
        assertEquals(Arrays.asList("JOHN", "JACK"), wordGameDAO.queryWordsWithLenght(4));

        wordGameDAO.addWords(new String[] {"Jane", "Wick"});
        assertEquals(Arrays.asList("JOHN", "JACK", "JANE", "WICK"), wordGameDAO.queryWordsWithLenght(4));
    }

    @Test
    void testReadFromFile() {
        wordGameDAO.addWordsSeperatedBy("src/main/resources//szövegek/testfile.txt", " ");
        assertEquals(Arrays.asList("TEST"), wordGameDAO.queryWordsWithLenght(4));
    }

    @Test
    void testLike() {
        wordGameDAO.addWords("John", "Jack");
        List<String> words = wordGameDAO.queryWordsWithLenghtAndLike(4, "Jo__");
        assertEquals(1, words.size());
        assertEquals("JOHN", words.get(0));
    }

}