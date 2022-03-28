package spring.corepractice.autowired;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.corepractice.AutoAppConfig;
import spring.corepractice.discount.DiscountPolicy;
import spring.corepractice.member.Grade;
import spring.corepractice.member.Member;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AllBeanTest {

    @Test
    void findAllBean() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);

        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(1L, "A", Grade.VIP);

        int fixDiscountPrice = discountService.discount(member, 15000, "fixDiscountPolicy");
        int rateDiscountPrice = discountService.discount(member, 15000, "rateDiscountPolicy");

        assertThat(fixDiscountPrice).isEqualTo(1000);
        assertThat(rateDiscountPrice).isEqualTo(1500);
    }

    static class DiscountService {

        // 스프링 컨테이너에 등록된 모든 DiscountPolicy 구현체를 Map 과 List 로 만들 수 있음
        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policyList;

        @Autowired
        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policyList) {
            this.policyMap = policyMap;
            this.policyList = policyList;
            System.out.println("-------------생성자-------------");
            System.out.println("policyMap = " + policyMap);
            System.out.println("policyList = " + policyList);
            System.out.println("------------------------------");
        }

        public int discount(Member member, int price, String discountCode) {
            DiscountPolicy discountPolicy = policyMap.get(discountCode);

            System.out.println("discountCode = " + discountCode);
            System.out.println("discountPolicy = " + discountPolicy);

            return discountPolicy.discount(member, price);
        }
    }
}
