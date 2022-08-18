package hello.proxy.pureporxy.concreteproxy.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class TimeProxy extends ConcreteLogic {

    private final ConcreteLogic target;

    @Override
    public String operation() {
        log.info("TimeProxy 실행");

        long startTime = System.currentTimeMillis();
        String result = target.operation();
        long endTime = System.currentTimeMillis();

        long resultTime = endTime - startTime;
        log.info("resultTime = {}", resultTime);

        return result;
    }
}
