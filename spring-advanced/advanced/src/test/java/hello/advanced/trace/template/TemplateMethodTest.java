package hello.advanced.trace.template;

import hello.advanced.trace.template.code.AbstractTemplate;
import hello.advanced.trace.template.code.SubClassLogic1;
import hello.advanced.trace.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateMethodTest {

    @Test
    @DisplayName("템플릿 메소드 미적용")
    void templateMethodV0() {
        logic1();
        logic2();
    }

    @Test
    @DisplayName("클래스를 만들어서 템플릿 메소드 적용")
    void templateMethodV1() {
        AbstractTemplate template1 = new SubClassLogic1();
        template1.execute();

        AbstractTemplate template2 = new SubClassLogic2();
        template2.execute();
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
