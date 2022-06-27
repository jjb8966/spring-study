package spring.dbaccess.repository;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import spring.dbaccess.connection.DBConnectionUtil;
import spring.dbaccess.domain.Member;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static spring.dbaccess.connection.DBConnectionUtil.*;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 repository;

    @BeforeEach
    void beforeEach() {
        // 기본 DriverManager -> 항상 커넥션을 생성하여 제공
        //DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USER, PW);

        // HikariDataSource(커넥션 풀) -> 커넥션 풀에 커넥션을 미리 생성한 후 제공
        // get connection = HikariProxyConnection@864326906 wrapping com.mysql.cj.jdbc.ConnectionImpl@788fcafb
        // -> ConnectionImpl@788fcafb 동일
        // 커넥션 사용 -> 풀에 반환 -> 커넥션 획득 (이 과정을 순차적으로 진행하므로 하나의 커넥션을 사용함)
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USER);
        dataSource.setPassword(PW);

        repository = new MemberRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException {
        //save
        Member member = new Member("memberV100", 10000);
        repository.save(member);

        //find
        Member findMember = repository.findById(member.getMemberId());
        log.info("member={}", member);
        log.info("findMember={}", findMember);
        log.info("member == findMember : {}", member == findMember);
        log.info("member.equals(findMember) : {}", member.equals(findMember));
        assertThat(findMember).isEqualTo(member);

        //update
        repository.update(member.getMemberId(), 20000);
        Member updateMember = repository.findById(member.getMemberId());
        assertThat(updateMember.getMoney()).isEqualTo(20000);

        //delete
        repository.delete(member.getMemberId());
//        Member notFound = repository.findById(member.getMemberId()); 예외 발생
        assertThrows(NoSuchElementException.class, () -> repository.findById(member.getMemberId()));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}