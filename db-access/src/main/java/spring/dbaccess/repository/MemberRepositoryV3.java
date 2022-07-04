package spring.dbaccess.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import spring.dbaccess.domain.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * 트랜잭션 - 트랜잭션 매니저
 * DataSourceUtils.getConnection()
 * DataSourceUtils.releaseConnection()
 */

@Slf4j
public class MemberRepositoryV3 {

    // 추상화(인터페이스)에 의존 -> 구현체가 바뀌어도 코드를 변경할 필요가 없음!
    private DataSource dataSource;

    public MemberRepositoryV3(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException {
        String sql = "insert into member values(?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();

            return member;
        } catch (Exception e) {
            log.info("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
//            pstmt.close();  // 여기서 exception 터지면?? -> con 종료할 수 없음
//            con.close();
        }
    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found. memberId = " + memberId);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate();

            log.info("resultSize = {}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, memberId);
            int resultSize = pstmt.executeUpdate();

            log.info("resultSize = {}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        // 주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야 함
        //JdbcUtils.closeConnection(con);
        // 커넥션을 직접 닫으면 커넥션이 유지되지 않아 트랜잭션이 제대로 수행되지 않음

        DataSourceUtils.releaseConnection(con, dataSource);
        // 커넥션을 닫는게 아니라 유지
        // 트랜잭션 동기화 매니저가 관리하는 커넥션이 없는 경우 커넥션을 닫음
    }

    private Connection getConnection() throws SQLException {
        // 주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야 함
        //Connection connection = dataSource.getConnection();

        Connection connection = DataSourceUtils.getConnection(dataSource);
        // 트랜잭션 동기화 매니저가 관리하는 커넥션이 있는 경우 반환
        // 만약 없다면 커넥션을 생성해서 반환
        // -> 하나의 트랜잭션에서 커넥션을 파라미터로 받지 않고도 같은 커넥션을 사용하는게 가능

        log.info("get connection = {}, class= {}", connection, connection.getClass());

        return connection;
    }
}
