package wordgame.chris.jpa;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "words")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String word;

    @Column(name = "word_len")
    private int length;

    @ElementCollection()
    @CollectionTable(
            name="descriptions",
            joinColumns=@JoinColumn(name="word_f_id")
    )
    @Column(name = "description")
    private Set<String> description = new HashSet<>();

    public Word(String word, Set<String> desc) {
        this(word);
        this.description = desc;
    }

    public Word(String word) {
        this.word = word.toUpperCase();
        this.length = word.length();
    }

    public Word() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word.toUpperCase();
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Set<String> getDesc() {
        return description;
    }

    public void setDesc(Set<String> desc) {
        this.description = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return Objects.equals(id, word1.id) && Objects.equals(word, word1.word) && Objects.equals(description, word1.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, word, description);
    }
}
