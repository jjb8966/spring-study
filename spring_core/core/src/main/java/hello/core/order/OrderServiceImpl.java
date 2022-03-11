package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService {

//    서비스(클라이언트)가 인터페이스뿐만 아니라 구현체에도 의존하고 있음 -> DIP 위반
//    private final MemberRepository memberRepository = new MemoryMemberRepository();
//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

    // 인터페이스에만 의존
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    // @RequiredArgsConstructor 안 쓴 경우
    // final 필드를 초기화하는 생성자 수동으로 작성
    // 생성자가 1개인 경우 @Autowired 생략해도 됨
   public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    //테스트용
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
