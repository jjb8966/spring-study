package hello.springtx.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;

    @Transactional
    public void order(Order order) throws NotEnoughMoneyException {
        log.info("order 호출");
        repository.save(order);
        log.info("결제 프로세스 진입");

        if (order.getUsername().equals("예외")) {
            log.info("시스템 예외(런타임 예외) 발생");
            throw new RuntimeException();
        } else if (order.getUsername().equals("잔고부족")) {
            log.info("비지니스 예외(체크 예외) 발생");
            order.setPayStatus("대기");
            throw new NotEnoughMoneyException("잔고가 부족합니다.");
        } else {
            log.info("정상 처리");
            order.setPayStatus("완료");
        }

        log.info("결제 프로세스 완료");
    }
}
