package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@Controller
public class SessionController {

    @RequestMapping("/session-info")
    @ResponseBody
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return "세션 없음";
        }

        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session 이름 = {}, value = {}", name, session.getAttribute(name)));

        log.info("sessionId = {}", session.getId());
        log.info("session.getMaxInactiveInterval() = {}", new Date(session.getMaxInactiveInterval()));
        log.info("session.getCreationTime() = {}", new Date(session.getCreationTime()));
        log.info("session.getLastAccessedTime() = {}", new Date(session.getLastAccessedTime()));
        log.info("session.isNew() = {}", session.isNew());

        return "세션 출력";
    }
}
