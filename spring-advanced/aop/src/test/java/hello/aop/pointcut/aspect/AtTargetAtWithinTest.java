package hello.aop.pointcut.aspect;

import hello.aop.member.annotation.ClassAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
@Import(AtTargetAtWithinTest.Config.class)
public class AtTargetAtWithinTest {

    @Autowired
    Child child;

    @Test
    void success() {
        log.info("child proxy = {}", child.getClass());
        child.childMethod();
        child.parentMethod();
    }

    @Configuration
    static class Config {

        @Bean
        public Parent parent() {
            return new Parent();
        }

        @Bean
        public Child child() {
            return new Child();
        }

        @Bean
        public AtTargetAtWithinAspect atTargetAtWithinAspect() {
            return new AtTargetAtWithinAspect();
        }
    }

    static class Parent {
        public void parentMethod() {
        }
    }

    @ClassAop
    static class Child extends Parent {
        public void childMethod() {
        }
    }

    @Slf4j
    @Aspect
    static class AtTargetAtWithinAspect {

        // @target -> @ClassAop 어노테이션이 붙은 클래스 + 부모 타입 모두 프록시 적용
        @Around("execution(* hello.aop..*(..)) && @target(hello.aop.member.annotation.ClassAop)")
        public Object atTarget(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@target] {}", joinPoint.getSignature());

            return joinPoint.proceed();
        }

        // @within -> @ClassAop 어노테이션이 붙은 클래스만 어노테이션 적용
        @Around("execution(* hello.aop..*(..)) && @within(hello.aop.member.annotation.ClassAop)")
        public Object atWithin(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@within] {}", joinPoint.getSignature());

            return joinPoint.proceed();
        }
    }

}
