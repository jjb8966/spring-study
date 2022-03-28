package spring.corepractice.beanfind;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.corepractice.member.MemberRepository;
import spring.corepractice.member.MemoryMemberRepository;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SameBeanTypeTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SameBeanTypeConfig.class);


    @Test
    void findByType() {
        assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean(MemberRepository.class));
    }

    @Test
    void findByName() {
        MemberRepository memberRepository = ac.getBean("memberRepository1", MemberRepository.class);

        assertThat(memberRepository).isInstanceOf(MemberRepository.class);
    }

    @Test
    void findAllBean() {
        Map<String, MemberRepository> beansOfType = ac.getBeansOfType(MemberRepository.class);

        for (String key : beansOfType.keySet()) {
            System.out.print("[key] : " + key);
            System.out.print(" [value] : " + beansOfType.get(key));
            System.out.println(" [beansOfType] : " + beansOfType);
        }

        assertThat(beansOfType.get("memberRepository1")).isInstanceOf(MemberRepository.class);
        assertThat(beansOfType.get("memberRepository2")).isInstanceOf(MemberRepository.class);
    }

    @Configuration
    static class SameBeanTypeConfig {

        @Bean
        public MemberRepository memberRepository1() {
            return new MemoryMemberRepository();
        }

        @Bean
        public MemberRepository memberRepository2() {
            return new MemoryMemberRepository();
        }
    }
}
