package spring.dbaccess.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionUtil {

    public static final String URL = "jdbc:mysql://localhost:3500/db-access";
    public static final String USER = "jjb";
    public static final String PW = "jjb";

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PW);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
