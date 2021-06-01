package wordgame.chris;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordGameJpaDAOTest {

    private EntityManager entityManager;
    private WordGameJpaDAO wordGameJpaDAO;

    @BeforeEach
    void init() {
        entityManager = getEntityManager();
        wordGameJpaDAO = new WordGameJpaDAO(entityManager);
        MariaDbDataSource mDs = MDataSource.getMariaDbDataSource();
        Flyway flyway = Flyway.configure().dataSource(mDs).load();
        flyway.clean();
        flyway.migrate();
    }

    private EntityManager getEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("wordgame-jpa");
        return factory.createEntityManager();
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