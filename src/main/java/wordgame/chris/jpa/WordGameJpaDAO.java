package wordgame.chris.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class WordGameJpaDAO {

    private EntityManager entityManager;

    public WordGameJpaDAO(EntityManager entityManager) {
       this.entityManager = entityManager;
    }

    public WordGameJpaDAO() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("wordgame-jpa");
        entityManager = factory.createEntityManager();
    }

    //Beszúr egy vagy több szót az adatbázisba. String split()-kor hasznos a varargs
    public void addWords(String... words) {
        entityManager.getTransaction().begin();
            for (String word : words) {
                entityManager.persist(new WordWithLength(word, word.length()));
            }
            entityManager.getTransaction().commit();
    }

    public void addWordsSeperatedBy(String file, String separator) {
        Path path = Path.of(file);
        try(BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(separator);
                addWords(words);
            }
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Can't read the file.", ioe);
        }
    }

    public List<String> queryWordsWithLenght(int length) {
        return entityManager.createQuery("SELECT w.word FROM WordWithLength w WHERE w.length = :length", String.class)
                .setParameter("length" ,length).getResultList();
    }

    public List<String> queryWordsWithLenghtAndLike(int length, String like) {
        String emptyLikePatternFromWordLength = emptyLikePatternMakerFromLength(length);
        if (like.equals(emptyLikePatternFromWordLength)) {
            return queryWordsWithLenght(length);
        }
        return entityManager.createQuery("SELECT w.word FROM WordWithLength w WHERE w.length  = :length AND w.word LIKE :like", String.class)
                .setParameter("length" ,length)
                .setParameter("like", like).getResultList();
    }

    private String emptyLikePatternMakerFromLength(int wordLength) {
        String like = "";
        for (int i = 0; i < wordLength; i++) {
            like += "_";
        }
        return like;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
