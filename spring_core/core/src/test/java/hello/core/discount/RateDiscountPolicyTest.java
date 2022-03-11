package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RateDiscountPolicyTest {

    DiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10% 할인이 적용되어야 한다.")
    void vip는_할인됨() {
        //given
        Member member = new Member(1L, "memberVIP", Grade.VIP);

        //when
        int discount = discountPolicy.discount(member, 15000);

        //then
        assertThat(discount).isEqualTo(1500);
    }

    @Test
    @DisplayName("VIP가 아니면 할인을 받을 수 없다.")
    void VIP_아니면_할인_안됨() {
        //given
        Member member = new Member(1L, "member", Grade.BASIC);

        //when
        int discount = discountPolicy.discount(member, 15000);

        //then
        assertThat(discount).isEqualTo(0);
    }
}