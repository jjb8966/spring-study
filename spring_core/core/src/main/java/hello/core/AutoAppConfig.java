package hello.core;

import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.springframework.context.annotation.ComponentScan.*;

@Configuration
@ComponentScan(
        // basePackages = "hello.core",
        // -> 지정하지 않으면 ComponentScan 이 붙은 클래스가 탐색할 시작 패키지가 됨
        // 보통 따로 지정하지 않고 ComponentScan 클래스를 프로젝트 최상단 패키지에 둔다.
        excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)

public class AutoAppConfig {

    // 이미 MemoryMemberRepository 를 @Component 로 지정한 상태
    // -> 스프링 빈으로 memoryMemberRepository 존재
//    @Bean(name = "memoryMemberRepository")
//    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//    }
}
