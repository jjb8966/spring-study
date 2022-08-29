package hello.aop.order.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* hello.aop.order..*(..))")
    public void allOrder() {

    }

    //~~Service로 끝나는 클래스, 인터페이스를 대상으로 적용
    @Pointcut("execution(* *..*Service.*(..))")
    public void allService() {

    }

    @Pointcut("allOrder() && allService()")
    public void orderAndService() {

    }
}
