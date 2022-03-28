package hello.servlet.domain.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest {

    MemberRepository memberRepository = MemberRepository.getInstance();

    @AfterEach
    void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void 회원_아이디로_조회() {
        //given
        Member member = new Member("Kim", 20);

        //when
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(member.getId());

        //then
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void 전체_회원_조회() {
        //given
        Member member1 = new Member("A", 20);
        Member member2 = new Member("B", 30);

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        List<Member> result = memberRepository.findAll();

        //then
        assertThat(result.size()).isSameAs(2);
        assertThat(result).contains(member1, member2);
    }
}