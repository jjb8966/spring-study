package hello.aop.internalcall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV2 {

    private final ApplicationContext applicationContext;
//    private final ObjectProvider<CallServiceV2> objectProvider;

    public void external() {
        log.info("call external");
        CallServiceV2 callService = applicationContext.getBean(CallServiceV2.class);
//        CallServiceV2 callService = objectProvider.getObject();

        callService.internal();
    }

    public void internal() {
        log.info("call internal");
    }
}
