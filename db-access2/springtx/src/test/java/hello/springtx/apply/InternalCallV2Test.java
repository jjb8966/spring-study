package hello.springtx.apply;

import lombok.RequiredArgsConstructor;
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
public class InternalCallV2Test {

    @Autowired private CallService callService;
    @Autowired private InternalService internalService;

    @Test
    @DisplayName("aop가 적용된 클래스인가?")
    void proxyCheck() {
        log.info("callService.getClass() = {}", callService.getClass());
        log.info("internalService.getClass() = {}", internalService.getClass());

        assertThat(AopUtils.isAopProxy(callService)).isFalse();
        assertThat(AopUtils.isAopProxy(internalService)).isTrue();
    }

    @Test
    void internalTest() {
        internalService.internal();
    }

    @Test
    void externalTest() {
        callService.external();
    }

    @TestConfiguration
    static class Config {

        @Bean
        CallService callService() {
            return new CallService(internalService());
        }

        @Bean
        InternalService internalService() {
            return new InternalService();
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    static class CallService {

        private final InternalService internalService;

        public void external() {
            log.info("start external");
            printTxInfo();
            //this.internal();
            internalService.internal();
            log.info("end external");
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("is txActive = {}", txActive);
        }
    }

    @Slf4j
    static class InternalService {

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
