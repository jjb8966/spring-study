package spring.corepractice.discount;

import org.springframework.stereotype.Component;
import spring.corepractice.annotation.MainDiscountPolicy;
import spring.corepractice.member.Grade;
import spring.corepractice.member.Member;

@Component
// @Primary
@MainDiscountPolicy
public class FixDiscountPolicy implements DiscountPolicy{

    private int discountFixAmount = 1000;

    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return discountFixAmount;
        }

        return 0;
    }
}
