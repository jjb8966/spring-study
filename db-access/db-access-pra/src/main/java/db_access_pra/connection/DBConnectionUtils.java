package db_access_pra.connection;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionUtils {

    private static String URL = "jdbc:h2:tcp://localhost/~/db-access-pra";
    private static String USER = "sa";
    private static String PW = "";

    public static Connection getConnection() throws SQLException {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(URL);
        hikariDataSource.setUsername(USER);
        hikariDataSource.setPassword(PW);

        return hikariDataSource.getConnection();
    }
}
