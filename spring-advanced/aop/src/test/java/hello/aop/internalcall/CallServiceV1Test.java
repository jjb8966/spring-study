package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV1Test {

    @Autowired
    CallServiceV1 callService;

    @Test
    void external() {   // 자기 자신(프록시)을 의존관계 주입받아 해결
        callService.external();
    }

    @Test
    void internal() {
        callService.internal();
    }
}