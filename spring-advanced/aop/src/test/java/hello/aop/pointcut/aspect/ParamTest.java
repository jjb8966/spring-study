package hello.aop.pointcut.aspect;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
@Import(ParamTest.ParamAspect.class)
public class ParamTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService proxy = {}", memberService.getClass());

        memberService.hello("helloA");
    }

    @Aspect
    static class ParamAspect {

        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember() {}

        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg = joinPoint.getArgs()[0];

            log.info("[joinPoint.getArgs()] {}, arg = {}", joinPoint.getSignature(), arg);

            return joinPoint.proceed();
        }

        @Around("allMember() && args(arg,..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[args(arg,..) - (Object arg)] {}, arg = {}", joinPoint.getSignature(), arg);

            return joinPoint.proceed();
        }

        @Around("allMember() && args(arg,..)")      // args(String, ..) -> 같은 효과
        public Object logArgs3(ProceedingJoinPoint joinPoint, String arg) throws Throwable {
            log.info("[args(arg,..) - (String arg)] {}, arg = {}", joinPoint.getSignature(), arg);

            return joinPoint.proceed();
        }

        @Before("allMember() && args(arg, ..)")
        public void logArgs4(String arg) {
            log.info("[args4], arg = {}", arg);
        }

        @Before("allMember() && this(obj)")     // 스프링 컨테이너에 등록된 프록시 객체
        public void thisArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[this(obj) - (MemberService obj)] {}, obj = {}", joinPoint.getSignature(), obj.getClass());
        }

        @Before("allMember() && target(obj)")   // 프록시가 적용될 target 객체
        public void targetArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[target(obj) - (MemberService obj)] {}, obj = {}", joinPoint.getSignature(), obj.getClass());
        }

        @Before("allMember() && @target(annotation)")
        public void atTarget(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@target(annotation) - (ClassAop annotation)] {}, annotation = {}", joinPoint.getSignature(), annotation);
        }

        @Before("allMember() && @within(annotation)")
        public void atWithin(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@within(annotation) - (ClassAop annotation)] {}, annotation = {}", joinPoint.getSignature(), annotation);
        }

        @Before("allMember() && @annotation(annotation)")
        public void atAnnotation(JoinPoint joinPoint, MethodAop annotation) {
            log.info("[@annotation(annotation) - (MethodAop annotation)] {}, annotation = {}, annotation value = {}", joinPoint.getSignature(), annotation, annotation.value());
        }
    }

}
