package hello.core.scope;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest2 {

    @Test
    public void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean bean1 = ac.getBean(ClientBean.class);
        ClientBean bean2 = ac.getBean(ClientBean.class);

        int count1 = bean1.logic();     // 1
        int count2 = bean2.logic();     // 1

        assertThat(count1).isSameAs(count2);

        // 스프링 컨테이너에 종속적인 코드가 되기 때문에 단위 테스트가 어려워짐
        // DL만 제공하는 무언가가 필요함 -> Provider
    }

    @RequiredArgsConstructor
    static class ClientBean {

        private final AnnotationConfigApplicationContext ac;

        public int logic() {
            // 의존관계를 외부에서 주입 받는게 아니라 직접 필요한 의존관계를 찾음
            // -> Dependency Lookup (DL)
            // 스프링 컨테이너에서 프로토타입 빈을 조회할 때 마다 새로운 빈 생성
            PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }
}
