package spring.dbaccess.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import spring.dbaccess.domain.Member;
import spring.dbaccess.repository.MemberRepositoryV3;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - @Transactional AOP
 */

@Slf4j
public class MemberServiceV3_3 {

//    private final TransactionTemplate transactionTemplate;  // -> 트랜잭션 매니저 필요함
    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_3(MemberRepositoryV3 memberRepository) {
//        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
//        transactionTemplate.executeWithoutResult((status) -> {
//            try {
//                bizLogic(fromId, toId, money); // 비지니스 로직
//            } catch (SQLException e) {
//                throw new IllegalStateException(e);
//            }
//        });

        bizLogic(fromId, toId, money); // 비지니스 로직
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
