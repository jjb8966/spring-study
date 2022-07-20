package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    // @Login 애노테이션이 Member 앞에 붙어있으면
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportParameter 실행");

        // @Login 애노테이션이 있는지
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);

        // 파라미터 타입이 Member 인지
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasMemberType;
    }

    // supportsParameter() 결과가 true 이면 실행됨
    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {

        log.info("@Login 어노테이션 resolverArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);

        if (session == null) {
            log.info("미인증 사용자");
            return null;
        }

        log.info("로그인한 사용자");
        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
