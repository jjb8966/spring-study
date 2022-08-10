package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.template.TimeLogTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateCallbackTest {

    @Test
    void callback() {
        TimeLogTemplate timeLogTemplate = new TimeLogTemplate();

        timeLogTemplate.execute(() -> log.info("비지니스 로직1"));
        timeLogTemplate.execute(() -> log.info("비지니스 로직2"));
    }
}
