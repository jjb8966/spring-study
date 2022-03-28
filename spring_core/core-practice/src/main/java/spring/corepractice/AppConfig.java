package spring.corepractice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.corepractice.discount.DiscountPolicy;
import spring.corepractice.discount.RateDiscountPolicy;
import spring.corepractice.member.MemberRepository;
import spring.corepractice.member.MemberService;
import spring.corepractice.member.MemberServiceImpl;
import spring.corepractice.member.MemoryMemberRepository;
import spring.corepractice.order.OrderService;
import spring.corepractice.order.OrderServiceImpl;

@Configuration      // 스프링 컨테이너가 설정 정보로 사용하는 클래스
public class AppConfig {

    // 메서드 명 : key -> 스프링 빈 이름
    // 리턴 객체 : value

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        //return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}
