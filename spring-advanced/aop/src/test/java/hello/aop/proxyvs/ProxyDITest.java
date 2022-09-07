package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import hello.aop.proxyvs.code.ProxyDIAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Import(ProxyDIAspect.class)
//@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"})   // JDK 동적 프록시 사용. 구체 클래스로 DI 시 예외 발생
//@SpringBootTest(properties = {"spring.aop.proxy-target-class=true"})   // CGLIB 사용
@SpringBootTest     // 스프링 부트 2.0부터는 CGLIB을 기본적으로 사용
class ProxyDITest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberServiceImpl memberServiceImpl;

    @Test
    void go() {
        log.info("memberService class = {}", memberService.getClass());
        log.info("memberServiceImpl class = {}", memberServiceImpl.getClass());
        memberServiceImpl.hello("hello");
    }

    /**
     * - JDK 동적 프록시의 한계
     * 옳바르게 설계된 애플리케이션이라면 구현체를 주입받는 경우가 매우 드물지만
     * 테스트 또는 여러 이유로 구체 클래스를 주입받아야 하는 경우
     * JDK 동적 프록시 기술로 생성된 인터페이스 기반 프록시는 예외를 발생시킴
     *
     * - CGLIB의 한계
     * 1. target 클래스의 기본 생성자가 필수
     * 2. 생성자가 2번 호출되는 문제 (target 클래스 객체 생성 시, 클래스 기반 프록시 객체 생성 시)
     * 3. final 키워드 사용 불가 (프록시 객체가 상속, 오버라이딩 할 수 없으므로)
     *
     * - 스프링의 해결책
     * objenesis라는 라이브러리를 사용해 CGLIB의 문제 1,2를 해결하여 CGLIB을 기본으로 사용함
     *      - final 키워드는 프레임워크를 만드는게 아니라면 개발 시 잘 사용하지 않으므로 크게 문제가 되지 않음
     */
}