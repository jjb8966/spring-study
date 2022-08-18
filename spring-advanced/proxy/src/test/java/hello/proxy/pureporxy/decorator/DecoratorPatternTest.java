package hello.proxy.pureporxy.decorator;

import hello.proxy.pureporxy.decorator.code.DecoratorPatternClient;
import hello.proxy.pureporxy.decorator.code.MessageDecorator;
import hello.proxy.pureporxy.decorator.code.RealComponent;
import hello.proxy.pureporxy.decorator.code.TimeDecorator;
import org.junit.jupiter.api.Test;

public class DecoratorPatternTest {

    @Test
    void noDecorator() {
        RealComponent realComponent = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(realComponent);
        client.execute();
    }

    @Test
    void decoratorV1() {
        RealComponent realComponent = new RealComponent();
        MessageDecorator messageDecorator = new MessageDecorator(realComponent);
        DecoratorPatternClient client = new DecoratorPatternClient(messageDecorator);
        client.execute();
    }

    @Test
    void decoratorV2() {
        // client -> timeDecorator (proxy) -> messageDecorator (proxy) -> realComponent
        // -> Proxy Chain
        RealComponent realComponent = new RealComponent();
        MessageDecorator messageDecorator = new MessageDecorator(realComponent);
        TimeDecorator timeDecorator = new TimeDecorator(messageDecorator);
        DecoratorPatternClient client = new DecoratorPatternClient(timeDecorator);
        client.execute();
    }
}
