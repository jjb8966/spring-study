package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.sql.DataSource;

@Slf4j
@SpringBootTest
public class BasicTxTest {

    @Autowired PlatformTransactionManager txManager;

    @TestConfiguration
    static class Config {

        @Bean
        PlatformTransactionManager txManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }

    @Test
    void commit() {
        log.info("트랜잭션 시작");
        TransactionStatus tx = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜잭션 커밋");
        txManager.commit(tx);
        log.info("트랜잭션 종료");
    }

    @Test
    void rollback() {
        log.info("트랜잭션 시작");
        TransactionStatus tx = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("트랜잭션 롤백");
        txManager.rollback(tx);
        log.info("트랜잭션 종료");
    }

    @Test
    void double_tx() {
        // 두 트랜잭션은 논리적으로 다른 커넥션을 사용하므로 서로 영향을 주지 않음
        log.info("=======트랜잭션1 시작=======");
        TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션1 커밋");
        txManager.commit(tx1);
        log.info("트랜잭션1 종료");

        log.info("=======트랜잭션2 시작=======");
        TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("트랜잭션2 롤백");
        txManager.rollback(tx2);
        log.info("트랜잭션2 종료");
    }

    @Test
    void inner_commit() {
        log.info("=======outer 시작=======");
        TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());

        log.info("=======inner 시작=======");
        TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
        log.info("inner 커밋");
        txManager.commit(inner);
        log.info("inner 종료");

        log.info("outer 커밋");
        txManager.commit(outer);
        log.info("outer 종료");
    }
}
