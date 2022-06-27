package spring.dbaccess.connection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import spring.dbaccess.connection.DBConnectionUtil;

import java.sql.Connection;

class DBConnectionUtilTest {

    @Test
    void test() {
        Connection connection = DBConnectionUtil.getConnection();
        Assertions.assertThat(connection).isNotNull();
    }
}