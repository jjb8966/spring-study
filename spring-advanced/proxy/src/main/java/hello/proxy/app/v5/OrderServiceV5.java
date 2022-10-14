package hello.proxy.app.v5;

import hello.proxy.trace.callback.TraceTemplate;
import hello.proxy.trace.logtrace.LogTrace;
import hello.proxy.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceV5 {

    private final OrderRepositoryV5 orderRepository;
    private final TraceTemplate template;

    public OrderServiceV5(OrderRepositoryV5 orderRepository, LogTrace logTrace) {
        this.orderRepository = orderRepository;
        this.template = new TraceTemplate(logTrace);
    }

    public void orderItem(String itemId) {
        template.execute("OrderService.orderItem", () -> {
            orderRepository.save(itemId);

            return null;
        });
    }
}