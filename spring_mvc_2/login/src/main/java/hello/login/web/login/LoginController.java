package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginForm form) {
        return "login/loginForm";
    }

    //@PostMapping("/login")
    public String loginV1(
            @ModelAttribute @Valid LoginForm form,
            BindingResult bindingResult,
            HttpServletResponse response) {

        // 입력 데이터 검증 실패
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        // 로그인 로직 수행
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 쿠키 사용
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);

        log.info("로그인한 멤버 : {}", loginMember);
        return "redirect:/";
    }

    //@PostMapping("/login")
    public String loginV2(
            @ModelAttribute @Valid LoginForm form,
            BindingResult bindingResult,
            HttpServletResponse response) {

        // 입력 데이터 검증 실패
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        // 로그인 로직 수행
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        // ObjectError 검증
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 직접 만든 sessionManager 사용
        sessionManager.createSession(loginMember, response);

        log.info("로그인한 멤버 : {}", loginMember);
        return "redirect:/";
    }

    //@PostMapping("/login")
    public String loginV3(
            @ModelAttribute @Valid LoginForm form,
            BindingResult bindingResult,
            HttpServletRequest request) {

        // 입력 데이터 검증 실패
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        // 로그인 로직 수행
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        // ObjectError 검증
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 서블릿이 제공하는 HttpSession 사용
        // response 대신 request 사용
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        log.info("로그인한 멤버 : {}", loginMember);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginV4(
            @ModelAttribute @Valid LoginForm form,
            BindingResult bindingResult,
            @RequestParam(defaultValue = "/") String redirectURL,
            HttpServletRequest request) {

        // 입력 데이터 검증 실패
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        // 로그인 로직 수행
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        // ObjectError 검증
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 서블릿이 제공하는 HttpSession 사용
        // response 대신 request 사용
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        log.info("로그인한 멤버 : {}", loginMember);
        return "redirect:" + redirectURL;
    }

    //@PostMapping("/logout")
    public String logoutV1(HttpServletResponse response) {
        expireCookie(response, "memberId");

        return "redirect:/";
    }

    //@PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        sessionManager.expire(request);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
