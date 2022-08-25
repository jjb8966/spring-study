package hello.proxy.config.v6_aop;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v6_aop.aspect.LogTraceAspect;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AopConfig {

    /**
     * 프록시 생성 과정
     * 1. 스프링 컨테이너가 @Aspect 붙은 스프링 빈 조회
     * 2. @Aspect 어드바이저 빌더가 @Aspect 정보를 기반으로 어드바이저 생성 후 빌더 내부에 저장
     * 3. 스프링 빈 대상이 되는 객체를 빈 후처리기(AutoProxyCreator)가 전달받으면 스프링 컨테이너와 @Aspect 어드바이저 빌더에서 어드바이저를 조회
     * 4. 어드바이저의 포인트컷을 통해 프록시가 필요한 객체인지 판별
     * 5. 프록시가 필요한 객체인 경우 프록시 생성 후 스프링 컨테이너에 등록
     */
    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {   // advisor 등록
                                                                // -> AnnotationAwareAspectJAutoProxyCreator가 프록시 만들어줌
        return new LogTraceAspect(logTrace);
    }
}
