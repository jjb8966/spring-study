package hello.proxy.pureporxy.decorator.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class MessageDecorator implements Component {

    private final Component target;

    @Override
    public String operation() {
        log.info("MessageDecorator 실행");

        String result = target.operation();
        String decoResult = "-----" + result + "-----";
        log.info("Deco 적용 전 = {}, Deco 적용 후 = {}", result, decoResult);

        return decoResult;
    }
}
