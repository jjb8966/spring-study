package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.ContextV1;
import hello.advanced.trace.strategy.code.strategy.Strategy;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV1Test {

    @Test
    @DisplayName("템플릿 메소드 미적용")
    void strategyV0() {
        logic1();
        logic2();
    }

    @Test
    @DisplayName("클래스를 만들어서 템플릿 메소드 적용")
    void strategyV1() {
        ContextV1 context1 = new ContextV1(new StrategyLogic1());
        context1.execute();

        ContextV1 context2 = new ContextV1(new StrategyLogic2());
        context2.execute();
    }

    @Test
    @DisplayName("익명 클래스 사용 - 1")
    void strategyV2() {
        Strategy strategy1 = new Strategy() {
            @Override
            public void call() {
                log.info("비지니스 로직1");
            }
        };
        ContextV1 context1 = new ContextV1(strategy1);
        context1.execute();

        Strategy strategy2 = new Strategy() {
            @Override
            public void call() {
                log.info("비지니스 로직2");
            }
        };
        ContextV1 context2 = new ContextV1(strategy2);
        context2.execute();
    }

    @Test
    @DisplayName("익명 클래스 사용 - 2")
    void strategyV3() {
        ContextV1 context1 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("비지니스 로직1");
            }
        });
        context1.execute();

        ContextV1 context2 = new ContextV1(new Strategy() {
            @Override
            public void call() {
                log.info("비지니스 로직2");
            }
        });
        context2.execute();
    }

    @Test
    @DisplayName("람다 사용")
    void strategyV4() {
        ContextV1 context1 = new ContextV1(() -> log.info("비지니스 로직1"));
        context1.execute();

        ContextV1 context2 = new ContextV1(() -> log.info("비지니스 로직2"));
        context2.execute();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();
        log.info("비지니스 로직1");
        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;
        log.info("resultTime = {}", resultTime);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();
        log.info("비지니스 로직2");
        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;
        log.info("resultTime = {}", resultTime);
    }
}
