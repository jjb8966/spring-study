package spring.corepractice.discount;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import spring.corepractice.member.Grade;
import spring.corepractice.member.Member;

class RateDiscountPolicyTest {

    DiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Test
    void VIP_회원은_10프로_할인() {
        //given
        Member member = new Member(1L, "A", Grade.VIP);

        //when
        int discountPrice = discountPolicy.discount(member, 15000);

        //then
        Assertions.assertThat(discountPrice).isEqualTo(1500);
    }
}