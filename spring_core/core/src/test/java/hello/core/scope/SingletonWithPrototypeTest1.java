package hello.core.scope;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest1 {

    @Test
    public void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean bean1 = ac.getBean(ClientBean.class);
        ClientBean bean2 = ac.getBean(ClientBean.class);

        int count1 = bean1.logic();     // 1
        int count2 = bean2.logic();     // 2

        assertThat(bean1.prototypeBean).isEqualTo(bean2.prototypeBean);
        assertThat(count1).isNotSameAs(count2);

        // 프로토타입을 쓰는 이유는 서로 다른 프로토타입 빈을 쓰기 위함인데
        // 싱글톤 빈이 멤버 필드로 프로토타입 빈을 가지고 있으면 처음 싱글톤 빈이 생성될 때
        // 프로토타입 빈이 호출되어 생성되고 2번째 호출할 때는 이미 생성된 싱글톤 빈을 가져오기 때문에
        // 동일한 프로토타입 빈을 갖게 된다.
    }

    @RequiredArgsConstructor
    static class ClientBean {

        private final PrototypeBean prototypeBean;

        public int logic() {
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }
}
