package spring.corepractice.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.corepractice.annotation.MainDiscountPolicy;
import spring.corepractice.discount.DiscountPolicy;
import spring.corepractice.member.Member;
import spring.corepractice.member.MemberRepository;

@Component
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;

    // fix, rate 둘다 @Component 로 등록된 경우
    // @Autowired -> ac.getBean(DiscountPolicy.class) 와 유사함
    // 자식 타입이 2개 이상이므로 에러가 발생
    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
