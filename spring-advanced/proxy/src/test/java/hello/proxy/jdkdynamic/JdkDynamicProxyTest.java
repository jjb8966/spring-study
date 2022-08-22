package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class JdkDynamicProxyTest {

    @Test
    void dynamicA() {
        AInterface target = new AImpl();                                    // 실제 로직을 수행할 target
        TimeInvocationHandler handler = new TimeInvocationHandler(target);  // 프록시에서 추가적으로 수행할 로직을 정의한 handler
        AInterface proxy = (AInterface) Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);
        // handler(+target)로 만든 프록시

        proxy.call();

        log.info("target.getClass() = {} ", target.getClass());
        log.info("proxy.getClass() = {} ", proxy.getClass());
    }

    @Test
    void dynamicB() {
        BInterface target = new BImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);
        BInterface proxy = (BInterface) Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]{BInterface.class}, handler);

        proxy.call();

        log.info("target.getClass() = {} ", target.getClass());
        log.info("proxy.getClass() = {} ", proxy.getClass());
    }
}
