package hello.proxy.config.v6_aop.aspect;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;

@RequiredArgsConstructor
@Slf4j
@Aspect     // advisor 자동 등록
public class LogTraceAspect {

    private final LogTrace logTrace;

    @Around("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog())")    // pointcut
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {                     // advice
        TraceStatus status = null;

        try {
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);
            Object result = joinPoint.proceed();
            logTrace.end(status);

            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }

}

