package wordgame.chris;

import org.mariadb.jdbc.MariaDbDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WordGameDAO {

    private MariaDbDataSource ds;

    public WordGameDAO() {
        try {
            MariaDbDataSource dataSource = new MariaDbDataSource();
            dataSource.setUrl("jdbc:mariadb://localhost:3306/employees?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("");
            ds = dataSource;
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can't connect to the database.", sqle);
        }
    }

    //Beszúr egy vagy több szót az adatbázisba. String split()-kor hasznos a varargs
    public void addWords(String... words) {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO wordgame (word, word_len) VALUES (?, ?)")) {
            for (String word : words) {
                ps.setString(1, word);
                ps.setInt(2, word.length());
                ps.executeUpdate();
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can't write in database.", sqle);
        }
    }

    public void addWordsSeperatedBySpaceFromFile(String file, String separator) {
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
        try(Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT word FROM wordgame WHERE word_len = ?")) {
            ps.setInt(1, length);
            try(ResultSet rs = ps.executeQuery()) {
                List<String> words = new ArrayList<>();
                while (rs.next()) {
                    words.add(rs.getString("word"));
                }
                return words;
            } catch (SQLException sqle) {
                throw new IllegalStateException("Can't read the database.");
            }

        } catch (SQLException sqle) {
            throw new IllegalStateException("Can't write in database.", sqle);
        }
    }

    public MariaDbDataSource getDs() {
        return ds;
    }
}
