package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV2Test {

    @Test
    @DisplayName("템플릿 메소드 미적용")
    void strategyV0() {
        logic1();
        logic2();
    }

    @Test
    @DisplayName("클래스를 만들어서 템플릿 메소드 적용")
    void strategyV1() {
        ContextV2 context = new ContextV2();
        context.execute(new StrategyLogic1());
        context.execute(new StrategyLogic2());
    }

    @Test
    @DisplayName("익명 클래스 사용")
    void strategyV2() {
        ContextV2 context = new ContextV2();
        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비지니스 로직1");
            }
        });

        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비지니스 로직2");
            }
        });
    }

    @Test
    @DisplayName("람다 사용")
    void strategyV3() {
        ContextV2 context = new ContextV2();
        context.execute(() -> log.info("비지니스 로직1"));
        context.execute(() -> log.info("비지니스 로직2"));
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
