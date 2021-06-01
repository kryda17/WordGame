package wordgame.chris.jpa;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;
import wordgame.chris.jpa.MDataSource;
import wordgame.chris.jpa.WordGameJpaDAO;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordGameJpaDAOTest {

    private WordGameJpaDAO wordGameJpaDAO;

    @BeforeEach
    void init() {
        wordGameJpaDAO = new WordGameJpaDAO();
        MariaDbDataSource mDs = MDataSource.getMariaDbDataSource();
        Flyway flyway = Flyway.configure().dataSource(mDs).load();
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void testAddWord() {
        wordGameJpaDAO.addWords("John");
        assertEquals(Arrays.asList("JOHN"), wordGameJpaDAO.queryWordsWithLenght(4));
    }

    @Test
    void testAddWordsWithVarargs() {
        wordGameJpaDAO.addWords("JOHN" , "JACK");
        assertEquals(Arrays.asList("JOHN", "JACK"), wordGameJpaDAO.queryWordsWithLenght(4));

        wordGameJpaDAO.addWords(new String[] {"Jane", "Wick"});
        assertEquals(Arrays.asList("JOHN", "JACK", "JANE", "WICK"), wordGameJpaDAO.queryWordsWithLenght(4));
    }

    @Test
    void testReadFromFile() {
        wordGameJpaDAO.addWordsSeperatedBy("src/main/resources//szövegek/testfile.txt", " ");
        assertEquals(Arrays.asList("TEST"), wordGameJpaDAO.queryWordsWithLenght(4));
    }

    @Test
    void testLike() {
        wordGameJpaDAO.addWords("John", "Jack");
        List<String> words = wordGameJpaDAO.queryWordsWithLenghtAndLike(4, "JO__");
        assertEquals(1, words.size());
        assertEquals("JOHN", words.get(0));
    }

}