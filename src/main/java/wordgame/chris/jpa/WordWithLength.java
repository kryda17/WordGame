package wordgame.chris.jpa;

import javax.persistence.*;

@Entity
@Table(name = "wordgame")
public class WordWithLength {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String word;

    @Column(name = "word_len")
    private int length;

    public WordWithLength(String word, int length) {
        this.word = word.toUpperCase();
        this.length = length;
    }

    public WordWithLength() {
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
}
