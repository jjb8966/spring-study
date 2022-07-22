package hello.exception;

import hello.exception.interceptor.LogInterceptor;
import hello.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //@Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LogFilter());
        filterFilterRegistrationBean.setOrder(1);
        filterFilterRegistrationBean.addUrlPatterns("/*" );
        filterFilterRegistrationBean.setDispatcherTypes(DispatcherType.ERROR); // 에러 요청 전용 필터
        // filterFilterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST); -> default

        return filterFilterRegistrationBean;
    }

    // 인터셉터는 DispatcherType으로 클라이언트의 요청와 에러 페이지 요청을 구분할 수 없음
    // 대신 URL 설정이 자유롭기 때문에 addPathPatterns(), excludePathPatterns() 메소드로 적절히 처리하면 됨
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 에러 전용 인터셉터
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/error", "/error-page/**");
    }
}
