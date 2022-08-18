package hello.proxy.pureporxy.decorator.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class DecoratorPatternClient {

    private final Component component;

    public void execute() {
        String result = component.operation();
        log.info("result = {}", result);
    }
}
