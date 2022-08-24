package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BasicTest {

    @Test
    void basicConfig() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(BasicConfig.class);
        // 빈 후처리기 도입 전
        // A a = ac.getBean("beanA", A.class);
        // a.helloA();

        // 빈 후처리기 도입 후
        B b = ac.getBean("beanA", B.class);
        b.helloB();

//        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean(B.class));
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean(A.class));
    }

    @Configuration
    static class BasicConfig {

        @Bean(name = "beanA")
        public A a() {
            return new A();
        }

        @Bean
        public AtoBPostProcessor atoBPostProcessor() {
            return new AtoBPostProcessor();
        }
    }

    @Slf4j
    static class A {
        public void helloA() {
            log.info("hello A");
        }
    }

    @Slf4j
    static class B {
        public void helloB() {
            log.info("hello B");
        }
    }

    @Slf4j
    static class AtoBPostProcessor implements BeanPostProcessor {

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("빈 후처리기 작동");
            log.info("매개변수 bean = {}, beanName = {}", bean.getClass(), beanName);

            if (bean instanceof A) {
                return new B();
            }

            return bean;
        }
    }

}
