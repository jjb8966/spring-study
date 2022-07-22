package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
public class InternalCallV1Test {

    @Autowired
    private CallService callService;

    @Test
    @DisplayName("aop가 적용된 클래스인가?")
    void proxyCheck() {
        log.info("callService.getClass() = {}", callService.getClass());
        assertThat(AopUtils.isAopProxy(callService)).isTrue();
    }

    @Test
    void internalTest() {
        callService.internal();
    }

    @Test
    void externalTest() {
        // internal() 메소드는 트랜잭션 애노테이션을 붙였음에도 불구하고
        // 트랜잭션이 제대로 적용되지 않음!
        callService.external();
    }

    @TestConfiguration
    static class Config {

        @Bean
        CallService callService() {
            return new CallService();
        }
    }

    @Slf4j
    static class CallService {

        public void external() {
            log.info("start external");
            printTxInfo();
            /**
             * 중요!!!!!!!!!!!
             * 클래스 내부에서 호출이 일어날 경우 프록시 객체를 통한 호출이 아니므로 트랜잭셕이 적용되지 않음!!
             * internal() == this.internal()
            */
            this.internal();
            log.info("end external");
        }

        @Transactional
        public void internal() {
            log.info("start internal");
            printTxInfo();
            log.info("end internal");
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("is txActive = {}", txActive);
        }
    }

}
