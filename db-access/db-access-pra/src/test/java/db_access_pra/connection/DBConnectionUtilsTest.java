package db_access_pra.connection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionUtilsTest {

    @Test
    void getConnection() throws SQLException {
        Connection connection = DBConnectionUtils.getConnection();
        Assertions.assertThat(connection).isNotNull();
    }
}