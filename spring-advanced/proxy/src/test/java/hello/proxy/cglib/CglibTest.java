package hello.proxy.cglib;

import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibTest {

    @Test
    void cglib() {
        ConcreteService target = new ConcreteService();
        TimeMethodInterceptor interceptor = new TimeMethodInterceptor(target);

        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(ConcreteService.class);
        enhancer.setCallback(interceptor);
        ConcreteService proxy = (ConcreteService) enhancer.create();

        proxy.call();

        log.info("target.getClass() = {}", target.getClass());
        log.info("proxy.getClass() = {}", proxy.getClass());
    }
}
