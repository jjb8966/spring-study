package hello.proxy.pureporxy.proxy.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class CacheProxy implements Subject{

    private final Subject target;

    private String cacheValue;

    @Override
    public String operation() {
        log.info("CacheProxy 실행");

        if (cacheValue == null) {
            cacheValue = target.operation();
        }

        return cacheValue;
    }
}
