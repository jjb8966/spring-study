package db_access_pra.repository;

import db_access_pra.connection.DBConnectionUtils;
import db_access_pra.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Slf4j
@Repository
public class MemberRepositoryV1 {

    public void save(Member member) {
        String sql = "insert into member " +
                "values(?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getName());
            pstmt.setLong(2, member.getMoney());
            pstmt.executeUpdate();
        } catch (Exception e) {
            log.info("error", e);
        } finally {
            close(con, pstmt, null);
        }
    }

    public void select(String name) {
        String sql = "select * from member where name = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            rs.next();

            log.info("member name = {}, money = {}", rs.getString("name"), rs.getLong("money"));
        } catch (Exception e) {
            log.info("error", e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    public void update(Member member) {
        String sql = "update member " +
                "set name = ?," +
                    "money = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBConnectionUtils.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getName());
            pstmt.setLong(2, member.getMoney());
            pstmt.executeUpdate();
        } catch (Exception e) {
            log.info("error", e);
        } finally {
            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        // stmt.close 예외가 발생해도 con 에 영향을 주지 않음
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }
}
