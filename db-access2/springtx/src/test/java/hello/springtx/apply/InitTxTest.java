package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class InitTxTest {

    @Autowired
    private BasicService basicService;

    @Test
    void initTest() {
        // 초기화 메소드가 실행 되는가
    }

    @TestConfiguration
    static class Config {

        @Bean
        BasicService basicService() {
            return new BasicService();
        }
    }

    @Slf4j
    static class BasicService {

        @PostConstruct
        @Transactional
        public void init1() {
            log.info("=======start init1=======");
            log.info("is txActive = {}", TransactionSynchronizationManager.isActualTransactionActive());
            log.info("=======end init1=======");
        }

        @EventListener(ApplicationReadyEvent.class)
        @Transactional
        public void init2() {
            log.info("=======start init2=======");
            log.info("is txActive = {}", TransactionSynchronizationManager.isActualTransactionActive());
            log.info("=======end init2=======");
        }
    }

}