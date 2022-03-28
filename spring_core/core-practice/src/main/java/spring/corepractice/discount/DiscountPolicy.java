package spring.corepractice.discount;

import spring.corepractice.member.Member;

public interface DiscountPolicy {

    int discount(Member member, int price);
}
