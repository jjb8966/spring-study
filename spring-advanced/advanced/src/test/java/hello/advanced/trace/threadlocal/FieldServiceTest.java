package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class FieldServiceTest {

    private FieldService fieldService = new FieldService();

    @Test
    void field() {
        log.info("main start");
        Runnable userA = () -> fieldService.logic("userA");
        Runnable userB = () -> fieldService.logic("userB");

        Thread threadA = new Thread(userA);
        Thread threadB = new Thread(userB);
        threadA.setName("thread-A");
        threadB.setName("thread-B");

        threadA.start();
        //sleep(2000);    // 동시성 문제 x
        sleep(100);  // 동시성 문제 o
        /**
         * 10:32:55.926 [main] INFO hello.advanced.trace.threadlocal.FieldServiceTest - main start
         * 10:32:55.929 [thread-A] INFO hello.advanced.trace.threadlocal.code.FieldService - 저장 name=userA -> nameStore=null
         * 10:32:56.030 [thread-B] INFO hello.advanced.trace.threadlocal.code.FieldService - 저장 name=userB -> nameStore=userA
         * 10:32:56.955 [thread-A] INFO hello.advanced.trace.threadlocal.code.FieldService - 조회 nameStore=userB
         * 10:32:57.036 [thread-B] INFO hello.advanced.trace.threadlocal.code.FieldService - 조회 nameStore=userB
         * 10:32:59.035 [main] INFO hello.advanced.trace.threadlocal.FieldServiceTest - main exit
         */
        threadB.start();

        sleep(3000);
        log.info("main exit");
    }

    private void sleep(int mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
