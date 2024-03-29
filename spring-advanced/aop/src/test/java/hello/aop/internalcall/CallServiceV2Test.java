package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV2Test {

    @Autowired
    CallServiceV2 callService;

    @Test
    void external() {   // ApplicationContext, ObjectProvider 등 지연 조회를 통해 해결
        callService.external();
    }

    @Test
    void internal() {
        callService.internal();
    }
}