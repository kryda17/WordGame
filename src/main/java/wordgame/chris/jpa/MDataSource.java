package wordgame.chris.jpa;

import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;

public class MDataSource {

    public static MariaDbDataSource getMariaDbDataSource() {
        MariaDbDataSource mDs = new MariaDbDataSource();
        try {
            mDs.setUrl("jdbc:mariadb://localhost:3306/employees?useUnicode=true");
            mDs.setUserName("root");
            mDs.setPassword("");
        } catch (SQLException sqlException) {
            throw new IllegalStateException("Can't connect to the database");
        }
        return mDs;
    }
}
