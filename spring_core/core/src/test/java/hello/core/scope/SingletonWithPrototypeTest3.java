package hello.core.scope;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest3 {

    @Test
    public void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean bean1 = ac.getBean(ClientBean.class);
        ClientBean bean2 = ac.getBean(ClientBean.class);

        int count1 = bean1.logic();     // 1
        int count2 = bean2.logic();     // 1

        assertThat(count1).isSameAs(count2);
    }

    @RequiredArgsConstructor
    static class ClientBean {

        private final ObjectProvider<PrototypeBean> provider;

        public int logic() {
            // 스프링 컨테이너에 종속적이지 않으면서 DL 기능만 제공해주는 ObjectProvider
            // 스프링에서 제공하는 기술이므로 스프링에 의존적
            // 만약 스프링이 아닌 다른 컨테이너에서 DL이 필요한 경우 Provider 를 사용해야 함
            PrototypeBean prototypeBean = provider.getObject();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }
}
