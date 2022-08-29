package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class AspectV6Advice {

//    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
//    public Object doTx(ProceedingJoinPoint joinPoint) throws Throwable {
//        try {
//            log.info("[Around][트랜잭션 시작] {}", joinPoint.getSignature());     // @Before
//            Object result = joinPoint.proceed();
//            log.info("[Around][트랜잭션 커밋] {}", joinPoint.getSignature());     // @AfterReturning
//
//            return result;
//        } catch (Exception e) {
//            log.info("[Around][트랜잭션 롤백] {}", joinPoint.getSignature());     // @AfterThrowing
//            throw e;
//        } finally {
//            log.info("[Around][리소스 릴리즈] {}", joinPoint.getSignature());     // @After
//        }
//    }

    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[Before][트랜잭션 시작] {}", joinPoint.getSignature());
    }

    @After("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[After][리소스 릴리즈] {}", joinPoint.getSignature());
    }

    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) {
        log.info("[AfterReturning][트랜잭션 커밋] {}", joinPoint.getSignature());
        log.info("Return result = {}", result);
    }

    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "exception")
    public void doThrowing(JoinPoint joinPoint, Exception exception) {
        log.info("[AfterThrowing][트랜잭션 롤백] {}", joinPoint.getSignature());
        log.info("Throwing exception message", exception.getMessage());
    }

}
