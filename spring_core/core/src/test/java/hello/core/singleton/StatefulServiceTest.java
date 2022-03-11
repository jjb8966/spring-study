package hello.core.singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

public class StatefulServiceTest {
    @Test
    @DisplayName("싱글톤 방식은 여러 클라이언트가 하나의 객체를 공유하기 때문에 stateful 하게 설계하면 안된다.")
    void statefulTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

        StatefulService statefulService1 = ac.getBean("statefulService", StatefulService.class);
        StatefulService statefulService2 = ac.getBean("statefulService", StatefulService.class);

        System.out.println("statefulService1 = " + statefulService1);
        System.out.println("statefulService2 = " + statefulService2);
        // -> 동일한 객체

        statefulService1.order("a", 10);
        statefulService2.order("b", 10000);
        // -> 동일한 객체이므로 price 는 공유되는 변수 (stateful) -> 다른 클라이언트에 의해 값이 변경될 수 있음
        // -> 공유 필드를 갖는 클래스에 싱글톤을 적용하면 큰 문제가 생길수 있음

        int priceA = statefulService1.getPrice();

        System.out.println("priceA = " + priceA);

        assertThat(priceA).isNotSameAs(10);
    }

    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }
}
