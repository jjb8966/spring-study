package spring.corepractice.autowired;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;
import spring.corepractice.member.Member;

import java.util.Optional;

public class AutowiredOptionTest {

    @Test
    void autowired_option() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutowiredConfig.class);
    }

    static class AutowiredConfig {

        //@Autowired(required = true) -> default
        // 자동 주입할 스프링 빈이 없는 경우 -> 에러 발생

        // Member 스프링 컨테이너에 등록 x

        // 자동 주입할 스프링 빈이 없는 경우 -> 메소드 호출 x
        @Autowired(required = false)
        public void setNoBean1(Member member) {
            System.out.println("AutowiredConfig.setNoBean1 : " + member);
        }

        // 자동 주입할 스프링 빈이 없는 경우 -> null 주입
        @Autowired
        public void setNoBean2(@Nullable Member member) {
            System.out.println("AutowiredConfig.setNoBean2 : " + member);
        }

        // 자동 주입할 스프링 빈이 없는 경우 -> Optional.empty 주입
        @Autowired
        public void setNoBean2(Optional<Member> member) {
            System.out.println("AutowiredConfig.setNoBean3 : " + member);
        }
    }
}
