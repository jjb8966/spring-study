package hello.core.member;

import hello.core.AutoAppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceImplTest {

    @Test
    @DisplayName("자동 주입 대상을 옵션으로 처리하는 방법")
    void autoWiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);

        MemberServiceImpl bean = ac.getBean(MemberServiceImpl.class);
    }
}