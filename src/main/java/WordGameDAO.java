import org.mariadb.jdbc.MariaDbDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
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
        } catch (SQLException sqlException) {
            throw new IllegalStateException("Can't connect to the database.");
        }
    }

    //Beszúr egy vagy több szót az adatbázisba. String split()-kor hasznos a varargs
    public void addWords(String... words) {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO wordgame (word) VALUES (?)")) {
            for (String word : words) {
                ps.setString(1, word);
                ps.executeUpdate();
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can't write in database.");
        }
    }

    public void addWordsSeperatedBySpaceFromFile(String file) {
        Path path = Path.of(file);
        try(BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(" ");
                addWords(words);
            }
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Can't read the file.");
        }
    }

    public List<String> queryWords() {
        try(Connection conn = ds.getConnection();
            Statement stmnt = conn.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT word FROM wordgame")) {

            List<String> words = new ArrayList<>();
            while(rs.next()) {
                words.add(rs.getString("word"));
            }
            return words;

        } catch (SQLException sqle) {
            throw new IllegalStateException("Can't write in database.");
        }
    }

    public MariaDbDataSource getDs() {
        return ds;
    }
}
