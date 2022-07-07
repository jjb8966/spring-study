package spring.dbaccess.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import spring.dbaccess.domain.Member;
import spring.dbaccess.repository.MemberRepositoryV3;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 템플릿
 */

@Slf4j
public class MemberServiceV3_2 {

    //    private final PlatformTransactionManager transactionManager;\
    private final TransactionTemplate transactionTemplate;  // -> 트랜잭션 매니저 필요함
    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.memberRepository = memberRepository;
    }

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
//        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        transactionTemplate.executeWithoutResult((status) -> {
            try {
                bizLogic(fromId, toId, money); // 비지니스 로직
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });

//        try {
//            bizLogic(fromId, toId, money); // 비지니스 로직
//
//            // 트랜잭션 종료
//            transactionManager.commit(status);
//        } catch (Exception e) {
//            // 트랜잭션 종료
//            transactionManager.rollback(status);
//            throw new IllegalStateException(e);
//        }
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
