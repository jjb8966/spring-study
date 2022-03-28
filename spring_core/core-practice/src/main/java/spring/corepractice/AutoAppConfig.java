package spring.corepractice;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        //basePackages = "spring.corepractice",
        // -> 생략하면 해당 설정 정보 클래스가 있는 패키지가 탐색 시작 패키지로 자동 지정됨
        // -> 보통 basePackages 를 생략하고 설정 정보 클래스를 프로젝트의 최상단에 위치시킴

        // Configuration 어노테이션이 붙은 AppConfig 도 자동 등록되므로 여기서 제외시켜줌
        // cf) @Configuration 에 @Component 포함되어 있음
        excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
// @Component -> OrderServiceImpl, MemberServiceImpl, MemoryMemberRepository, RateDiscountPolicy
public class AutoAppConfig {

    // 수동 등록 빈(@Bean) vs 자동 등록 빈(@Component) -> 수동 등록 빈이 우선 (오버라이딩 해버림)
//    @Bean("memoryMemberRepository")
//    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//    }

    /*
     * 스프링 부트 에러 발생 -> 애매한 상황을 만들지 않기 위해
     ****************************
     * APPLICATION FAILED TO START
     * ***************************
     *
     * Description:
     *
     * The bean 'memoryMemberRepository', defined in class path resource [spring/corepractice/AutoAppConfig.class], could not be registered.
     * A bean with that name has already been defined in file [/Users/joojongbum/Desktop/project/spring/spring_core/core-practice/out/production/classes/spring/corepractice/member/MemoryMemberRepository.class] and overriding is disabled.
     *
     * Action:
     *
     * Consider renaming one of the beans or enabling overriding by setting spring.main.allow-bean-definition-overriding=true
     */
}
