package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV3Test {

    @Autowired
    CallServiceV3 callService;

    @Test
    void external() {   // 내부호출 자체를 하지 않도록 구조를 변경하여 해결
        callService.external();
    }

//    @Test
//    void internal() {
//        callService.internal();
//    }
}