package spring.corepractice.singleton;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.corepractice.AppConfig;
import spring.corepractice.member.MemberRepository;
import spring.corepractice.member.MemberServiceImpl;
import spring.corepractice.order.OrderServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonContainerTest {

    @Test
    void 스프링_컨테이너는_싱글톤_컨테이너() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean(MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean(OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean(MemberRepository.class);

        // 모두 같은 인스턴스
        System.out.println("memberService -> memberRepository : " + memberService.getMemberRepository());
        System.out.println("orderService -> memberRepository : " + orderService.getMemberRepository());
        System.out.println("memberRepository : " + memberRepository);

        System.out.println("AppConfig : " + ac.getBean(AppConfig.class));
        // 출력 : spring.corepractice.AppConfig$$EnhancerBySpringCGLIB$$a241884e@8c11eee
        // -> 스프링 컨테이너에 등록된 빈은 AppConfig 클래스가 아닌 바이트코드가 조작된 임의의 클래스로 만든 빈
        // -> 조작된 임의의 클래스가 싱글톤을 보장해주는 것

        assertThat(memberService.getMemberRepository()).isEqualTo(memberRepository);
        assertThat(orderService.getMemberRepository()).isEqualTo(memberRepository);
    }
}
