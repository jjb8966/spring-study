package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV0Test {

    @Autowired
    CallServiceV0 callService;

    @Test
    void external() {
        callService.external();

        /**
         * aop = void hello.aop.internalcall.CallServiceV0.external()
         * call external
         * call internal
         * -> internal 메소드는 aop 적용 x
         */
    }

    @Test
    void internal() {
        callService.internal();
    }
}