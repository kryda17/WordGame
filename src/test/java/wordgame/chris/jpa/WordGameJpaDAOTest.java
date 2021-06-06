package wordgame.chris.jpa;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;
import wordgame.chris.jpa.MDataSource;
import wordgame.chris.jpa.WordGameJpaDAO;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordGameJpaDAOTest {

    private WordGameJpaDAO wordGameJpaDAO;

    @BeforeEach
    void init() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("wordgame-jpa");
        wordGameJpaDAO = new WordGameJpaDAO(entityManagerFactory);
        MariaDbDataSource mDs = MDataSource.getMariaDbDataSource();
        Flyway flyway = Flyway.configure().dataSource(mDs).load();
        flyway.clean();
        flyway.migrate();
    }


    @Test
    void testAddWordWithDescription() {
        wordGameJpaDAO.addWords(new Word("John", Set.of("Leírás1")));
        Word word = wordGameJpaDAO.queryWord("John");
        assertEquals("JOHN", word.getWord());
        assertEquals(Set.of("Leírás1"), word.getDesc());

        wordGameJpaDAO.addWords(new Word("Jack", Set.of("Leírás1", "Leírás2")));
        Word word2 = wordGameJpaDAO.queryWord("Jack");
        assertEquals("JACK", word2.getWord());
        assertEquals(Set.of("Leírás1", "Leírás2"), word2.getDesc());
    }

    @Test
    void testAddWordWithoutDescription() {
        wordGameJpaDAO.addWords(new Word("JACK"));
        Word word = wordGameJpaDAO.queryWord("Jack");
        assertEquals("JACK", word.getWord());
        assertEquals(0, word.getDesc().size());
    }

    /*@Test
    void testAddWordWithDesc() {
        wordGameJpaDAO.addWords(new Word("John", Set.of("Leírás1")));
        wordGameJpaDAO.addWords(new Word("Jacky", Set.of("Leírás2")));
        assertEquals("JOHN", wordGameJpaDAO.queryWordsWithLenght(4).get(0).getWord());
        assertEquals(Set.of("Leírás1"), wordGameJpaDAO.queryWord("JOHN").getDesc());
    }

     */

    @Test
    void testAddWordsWithVarargs() {
        Word word = new Word("John", Set.of("Leírás1"));
        Word word2 = new Word("Jack", Set.of("Leírás2"));
        wordGameJpaDAO.addWords(word, word2);

        Word qWord = wordGameJpaDAO.queryWord("John");
        assertEquals("JOHN", qWord.getWord());
        assertEquals(Set.of("Leírás1"),qWord.getDesc());
        Word qWord2 = wordGameJpaDAO.queryWord("Jack");
        assertEquals("JACK", qWord2.getWord());
        assertEquals(1, qWord2.getDesc().size());

    }

    @Test
    void testAddWordsWithVarargsArray() {
        Word word = new Word("John", Set.of("Leírás1", "Leírás2"));
        Word word2 = new Word("Jack", Set.of("Leírás3", "Leírás4"));
        wordGameJpaDAO.addWords(new Word[] {word, word2});
        Word qWord = wordGameJpaDAO.queryWord("John");
        assertEquals("JOHN", qWord.getWord());
        assertEquals(Set.of("Leírás1", "Leírás2"),qWord.getDesc());
        Word qWord2 = wordGameJpaDAO.queryWord("Jack");
        assertEquals("JACK", qWord2.getWord());
        assertEquals(2, qWord2.getDesc().size());
    }

    @Test
    void testReadFromFile() {
        wordGameJpaDAO.addWordsFromFile("src/main/resources//szövegek/testfile.txt", " ", ";");
        assertEquals("TEST", wordGameJpaDAO.queryWord("Test").getWord());
        Word word = wordGameJpaDAO.queryWord("test2");
        assertEquals("TEST2", word.getWord());
        assertEquals(1, word.getDesc().size());
        Word word2 = wordGameJpaDAO.queryWord("test3");
        assertEquals("TEST3", word2.getWord());
        assertEquals(3, word2.getDesc().size());

    }

    @Test
    void testLike() {
        Word word = new Word("John", Set.of("Leírás1", "Leírás2"));
        Word word2 = new Word("Jack", Set.of("Leírás3", "Leírás4"));
        wordGameJpaDAO.addWords(word, word2);
        List<Word> words = wordGameJpaDAO.queryWordsWithLike("JO__");
        assertEquals(1, words.size());
        assertEquals("JOHN", words.get(0).getWord());
    }

    @Test
    void queryWordTest() {
        Word word = new Word("John", Set.of("Leírás1", "Leírás2"));
        wordGameJpaDAO.addWords(word);
        assertTrue(word.equals(wordGameJpaDAO.queryWord("John")));
    }

    @Test
    void queryWordsWithLenghtTest() {
        Word word = new Word("John", Set.of("Leírás1", "Leírás2"));
        Word word2 = new Word("Joe", Set.of("Leírás3", "Leírás4"));
        wordGameJpaDAO.addWords(word, word2);
        assertEquals(1, wordGameJpaDAO.queryWordsWithLenght(4).size());
    }

}