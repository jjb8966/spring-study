package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

@Slf4j
public class ProxyCastingTest {

    @Test
    void jdkProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false);    // JDK 동적 프록시 사용

        Object proxy = proxyFactory.getProxy();

        // proxy -> 인터페에스 (O)
        MemberService memberService = (MemberService) proxy;

        // proxy -> 구체 클래스 (X)
        // => 의존관계 주입 시 스프링 컨테이너에 등록된 프록시를 구체 클래스로 주입받을 수 없음!!
        Assertions.assertThatThrownBy(() -> {
                    MemberServiceImpl memberServiceImpl = (MemberServiceImpl) proxy;
                })
                .isInstanceOf(ClassCastException.class);
    }

    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true);    // CGLIB 사용

        Object proxy = proxyFactory.getProxy();

        // proxy -> 인터페에스 (O)
        MemberService memberService = (MemberService) proxy;

        // proxy -> 구체 클래스 (O)
        MemberServiceImpl memberServiceImpl = (MemberServiceImpl) proxy;
    }

}
