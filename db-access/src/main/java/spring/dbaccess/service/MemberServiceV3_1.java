package spring.dbaccess.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import spring.dbaccess.domain.Member;
import spring.dbaccess.repository.MemberRepositoryV2;
import spring.dbaccess.repository.MemberRepositoryV3;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 매니저
 */

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {

    // private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;
    private final MemberRepositoryV3 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
//        Connection con = dataSource.getConnection();

        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        /**
         *  1. 내부에서 dataSource 를 사용해 커넥션을 생성
         *  2. 커넥션을 수동 커밋 모드로 변경
         *  3. 트랜잭션 동기화 매니저에 보관
         *      - 트랜잭션 동기화 매니저가 쓰레드 로컬에서 커넥션을 보관 - 멀티 쓰레드 환경에서도 안전
         *  4. repository에서 메소드 실행 시 트랜잭션 동기화 매니저에 보관된 동기화된 커넥션을 조회하여 사용
         *      - 커넥션을 파라미터로 받지 않아도 되는 이유
         *  5. 획득한 커넥션으로 sql을 db에 전달해 실행
         */

        try {
//            con.setAutoCommit(false);   // 트랜잭션 시작

//            bizLogic(con, fromId, toId, money); // 비지니스 로직
            bizLogic(fromId, toId, money); // 비지니스 로직

//            con.commit();

            // 트랜잭션 종료
            transactionManager.commit(status);
        } catch (Exception e) {
            // 트랜잭션 종료
            transactionManager.rollback(status);
            throw new IllegalStateException(e);
        } finally {
//            release(con); -> 트랜잭션 매니저가 알아서 리소스 닫아줌
            /**
             * 1. 비지니스 로직을 모두 마친 트랜잭션의 동기화된 커넥션을 조회
             * 2. 획득한 커넥션으로 commit or rollback
             * 3. 전체 리소스를 정리
             *      a. con.setAutCommit(true)
             *      b. con.close()
             */
        }

    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }
}
