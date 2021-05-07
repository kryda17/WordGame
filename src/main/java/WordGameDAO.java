import org.mariadb.jdbc.MariaDbDataSource;

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

    public void createWord(String word) {
        try(Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO wordgame (word) VALUES (?)")) {
            ps.setString(1, word);
            ps.executeUpdate();

        } catch (SQLException sqle) {
            throw new IllegalStateException("Can't write in database.");
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
