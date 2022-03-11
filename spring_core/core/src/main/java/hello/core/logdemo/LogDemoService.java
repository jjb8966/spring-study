package hello.core.logdemo;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogDemoService {

    private final MyLogger myLogger;

    //private final ObjectProvider<MyLogger> provider;

    public void logic(String id) {
        //MyLogger myLogger = provider.getObject();
        myLogger.log("service id : " + id);
    }
}
