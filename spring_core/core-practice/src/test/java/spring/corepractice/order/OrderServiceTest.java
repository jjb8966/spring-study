package spring.corepractice.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.corepractice.AppConfig;
import spring.corepractice.member.Grade;
import spring.corepractice.member.Member;
import spring.corepractice.member.MemberService;

class OrderServiceTest {

    MemberService memberService;
    OrderService orderService;

    @BeforeEach
    void setup() {
        // spring 사용 전
//        AppConfig appConfig = new AppConfig();
//        memberService = appConfig.memberService();
//        orderService = appConfig.orderService();

        // spring 사용 후
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        memberService = ac.getBean(MemberService.class);    // 이름 없이 클래스만 사용해서 찾을 수도 있음
        orderService = ac.getBean("orderService", OrderService.class);
    }

    @Test
    void 주문() {
        Member member = new Member(1L, "A", Grade.VIP);
        memberService.join(member);
        Order order = orderService.createOrder(member.getId(), "itemA", 10_000);

        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }
}