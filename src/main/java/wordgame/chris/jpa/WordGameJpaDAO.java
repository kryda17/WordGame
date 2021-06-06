package wordgame.chris.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordGameJpaDAO {

    private EntityManagerFactory entityManagerFactory;

    public WordGameJpaDAO(EntityManagerFactory entityManagerFactory) {
       this.entityManagerFactory = entityManagerFactory;
    }

    public WordGameJpaDAO() {
        entityManagerFactory = Persistence.createEntityManagerFactory("wordgame-jpa");
    }

    //Beszúr egy vagy több szót az adatbázisba. String split()-kor hasznos a varargs
    public void addWords(Word... words) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
            for (Word word : words) {
                entityManager.persist(word);
            }
        entityManager.getTransaction().commit();
        entityManager.close();
    }



    public void addWordsFromFile(String file, String wordSeparator, String descSeparator) {
        Path path = Path.of(file);
        try(BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(wordSeparator);
                if (words.length == 2) {
                    addWords(new Word(words[0], descriptionSplitter(words[1], descSeparator)));
                } else {
                    addWords(new Word(words[0]));
                }
            }
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Can't read the file.", ioe);
        }
    }

    private Set<String> descriptionSplitter(String desc, String descSeparator) {
        Set<String> descriptions = new HashSet<>();
        String[] split = desc.split(descSeparator);
        for (String item : split) {
            descriptions.add(item);
        }
        return descriptions;
    }



    public List<Word> queryWordsWithLenght(int length) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Word> words = entityManager.createQuery("SELECT w FROM Word w WHERE w.length = :length", Word.class)
                .setParameter("length" ,length).getResultList();
        entityManager.close();
        return words;
    }

    public List<Word> queryWordsWithLike(String like) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        //String emptyLikePatternFromWordLength = emptyLikePatternMakerFromLength(length);
        //if (like.equals(emptyLikePatternFromWordLength)) {
          //  return queryWordsWithLenght(length);
        //}
        int length = like.length();
        if (isEmptyLike(like)) {
            return queryWordsWithLenght(length);
        }
        List<Word> words = entityManager.createQuery("SELECT w FROM Word w WHERE w.length  = :length AND w.word LIKE :like", Word.class)
                .setParameter("length" , length)
                .setParameter("like", like).getResultList();
        entityManager.close();
        return words;
    }

    /*private String emptyLikePatternMakerFromLength(int wordLength) {
        String like = "";
        for (int i = 0; i < wordLength; i++) {
            like += "_";
        }
        return like;
    }

     */

    private boolean isEmptyLike(String like) {
        String emptyLike = "";
        for (int i = 0; i < like.length(); i++) {
            emptyLike += "_";
        }
        if (emptyLike.equals(like)) {
            return true;
        }
        return false;
    }

    public Word queryWord(String word) {
        return entityManagerFactory.createEntityManager().createQuery("select w from Word w left join fetch w.description where w.word = :word", Word.class)
                .setParameter("word", word.toUpperCase()).getSingleResult();
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
