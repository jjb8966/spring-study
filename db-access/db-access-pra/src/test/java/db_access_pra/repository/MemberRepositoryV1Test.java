package db_access_pra.repository;

import db_access_pra.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class MemberRepositoryV1Test {

    @Autowired
    MemberRepositoryV1 memberRepositoryV1;

    @Test
    void save() {
        Member member = new Member("a", 1L);
        memberRepositoryV1.save(member);
    }

    @Test
    void select() {
        memberRepositoryV1.select("b");
    }

    @Test
    void update() {
        Member member = new Member("b", 2L);
        memberRepositoryV1.update(member);
    }
}