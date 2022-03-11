package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.*;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), getDiscountPolicy());
    }

    // MemberRepository(역할)로 MemoryMemberRepository(구현체)를 썼음을 한눈에 알 수 있음
    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    // DiscountPolicy(역할)로 RateDiscountPolicy(구현체)를 썼음을 한눈에 알 수 있음
    // DiscountPolicy(역할)의 구현체를 바꾸고 싶으면 이 부분만 바꾸면 됨. 클라이언트 변경x
    @Bean
    public DiscountPolicy getDiscountPolicy() {
        // return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}
