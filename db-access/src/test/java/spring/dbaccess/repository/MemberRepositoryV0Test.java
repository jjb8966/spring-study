package spring.dbaccess.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import spring.dbaccess.domain.Member;
import spring.dbaccess.repository.MemberRepositoryV0;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        //save
        Member member = new Member("memberV100", 10000);
        repository.save(member);

        //find
        Member findMember = repository.findById(member.getMemberId());
        log.info("member={}", member);
        log.info("findMember={}", findMember);
        log.info("member == findMember : {}", member == findMember);
        log.info("member.equals(findMember) : {}", member.equals(findMember));
        assertThat(findMember).isEqualTo(member);

        //update
        repository.update(member.getMemberId(), 20000);
        Member updateMember = repository.findById(member.getMemberId());
        assertThat(updateMember.getMoney()).isEqualTo(20000);

        //delete
        repository.delete(member.getMemberId());
//        Member notFound = repository.findById(member.getMemberId()); 예외 발생
        assertThrows(NoSuchElementException.class, () -> repository.findById(member.getMemberId()));
    }
}