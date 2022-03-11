package hello.core.member;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    //서비스(클라이언트)가 인터페이스뿐만 아니라 구현체에도 의존하고 있음 -> DIP 위반
    //private final MemberRepository memberRepository = new MemoryMemberRepository();

    // 인터페이스에만 의존
    private final MemberRepository memberRepository;

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    // 테스트용
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }

    // 자동 주입 대상을 옵션으로 처리하는 방법
    @Autowired(required = false)
    public void setNoBean1(Member setNoBean1) {
        System.out.println("setNoBean1 = " + setNoBean1);
    }

    @Autowired
    public void setNoBean2(@Nullable Member setNoBean2) {
        System.out.println("setNoBean2 = " + setNoBean2);
    }

    @Autowired
    public void setNoBean3(Optional<Member> setNoBean3) {
        System.out.println("setNoBean3 = " + setNoBean3);
    }
}
