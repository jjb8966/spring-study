package spring.corepractice.beanfind;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.corepractice.discount.DiscountPolicy;
import spring.corepractice.discount.FixDiscountPolicy;
import spring.corepractice.discount.RateDiscountPolicy;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SameParentTypeTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SameParentTypeConfig.class);

    @Test
    void findByParentType() {
        assertThrows(NoSuchBeanDefinitionException.class,() -> ac.getBean(DiscountPolicy.class));
    }

    @Test
    void findByName() {
        DiscountPolicy discountPolicy = ac.getBean("discountPolicy1", DiscountPolicy.class);

        assertThat(discountPolicy).isInstanceOf(DiscountPolicy.class);
    }

    @Test
    void findAllBean() {
        Map<String, Object> beansOfType = ac.getBeansOfType(Object.class);

        for (String key : beansOfType.keySet()) {
            System.out.print("[key] : " + key);
            System.out.println(" [value] : " + beansOfType.get(key));
        }

        DiscountPolicy discountPolicy = ac.getBean("discountPolicy2", DiscountPolicy.class);

        assertThat(discountPolicy).isInstanceOf(DiscountPolicy.class);
    }

    @Configuration
    static class SameParentTypeConfig {

        @Bean
        public DiscountPolicy discountPolicy1() {
            return new FixDiscountPolicy();
        }

        @Bean
        public DiscountPolicy discountPolicy2() {
            return new RateDiscountPolicy();
        }
    }
}
