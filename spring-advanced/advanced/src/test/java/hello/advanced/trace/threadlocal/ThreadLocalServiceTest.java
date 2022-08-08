package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ThreadLocalServiceTest {

    private ThreadLocalService service = new ThreadLocalService();

    @Test
    void field() {
        log.info("main start");
        Runnable userA = () -> service.logic("userA");
        Runnable userB = () -> service.logic("userB");

        Thread threadA = new Thread(userA);
        Thread threadB = new Thread(userB);
        threadA.setName("thread-A");
        threadB.setName("thread-B");

        threadA.start();
        //sleep(2000);    // 동시성 문제 x
        sleep(100);  // 동시성 문제 o
        /**
         * 10:32:27.661 [main] INFO hello.advanced.trace.threadlocal.ThreadLocalServiceTest - main start
         * 10:32:27.663 [thread-A] INFO hello.advanced.trace.threadlocal.code.ThreadLocalService - 저장 name=userA -> nameStore=null
         * 10:32:27.769 [thread-B] INFO hello.advanced.trace.threadlocal.code.ThreadLocalService - 저장 name=userB -> nameStore=null
         * 10:32:28.677 [thread-A] INFO hello.advanced.trace.threadlocal.code.ThreadLocalService - 조회 nameStore=userA
         * 10:32:28.774 [thread-B] INFO hello.advanced.trace.threadlocal.code.ThreadLocalService - 조회 nameStore=userB
         * 10:32:30.774 [main] INFO hello.advanced.trace.threadlocal.ThreadLocalServiceTest - main exit
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
