package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationSingletonTest {

    @Test
    @DisplayName("")
    void configuration_test() {
        // @Configuration 이 적용된 AppConfig 클래스 사용
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean(MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean(OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean(MemberRepository.class);
        // -> call AppConfig.memberRepository 한번만 호출됨

        System.out.println("memberService -> memberRepository = " + memberService.getMemberRepository());
        System.out.println("orderService -> memberRepository = " + orderService.getMemberRepository());
        System.out.println("memberRepository = " + memberRepository);
        // -> 셋 다 동일

        assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);

        // AppConfig도 스프링 빈으로 등록되기 때문에 꺼낼 수 있음
        AppConfig appConfig = ac.getBean(AppConfig.class);
        System.out.println(appConfig);
        
        /** 출력 : hello.core.AppConfig$$EnhancerBySpringCGLIB$$eedbbb25@7b5a12ae
        * $$~~~ -> 실제로는 AppConfig 클래스의 객체가 만들어진게 아니라 임의의 다른 클래스를 만들고 스프링 빈으로 등록된 것!
        * 스프링이 CGLIB 라는 바이트코드 조작 라이브러리를 사용해 AppConfig 를 상속받은 임의의 클래스로 스프링 빈에 등록했음
        * 스프링 빈으로 등록된 클래스는 상속받은 클래스에 @Bean 메소드를 오버라이드하는데
        * 스프링 빈이 존재하면 존재하는 빈을 반환하고
        * 스프링 빈이 존재하지 않으면 생성해서 스프링 빈을 등록하고 반환한다.

        @Bean
        public MemberRepository memberRepository () {
            if (memoryMemberRepository 가 이미 스프링 컨테이너에 등록되어 있으면 ?){
                return 스프링 컨테이너에서 찾아서 반환;
            } else{ //스프링 컨테이너에 없으면
                기존 로직을 호출해서 MemoryMemberRepository 를 생성하고 스프링 컨테이너에 등록 return 반환
            }
        }

         * 만약 @Configuration 을 사용하지 않는다면? -> CGLIB을 사용하지 않고 순수 AppConfig 클래스의 객체로 스프링 빈에 등록됨
         * 그럼 위와 같이 스프링 컨테이너에 중복된 빈이 생성되게 됨
         * -> @Configuration 을 사용하지 않으면 순수 자바코드로 new memberRepository()가 3번 실행되게 되어
         *   call AppConfig.memberRepository 도 3번 호출되게 됨
         *
         *   항상 @Configuration을 사용하자!
         */
    }
}
