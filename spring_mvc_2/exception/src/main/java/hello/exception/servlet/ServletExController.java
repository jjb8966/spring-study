package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class ServletExController {

    @GetMapping("/ok")
    @ResponseBody
    public String normal() {
        return "정상 요청";
    }

    @GetMapping("/error-ex")
    public void errorEx() {
        throw new RuntimeException("예외 발생");
    }

    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 오류 발생");
    }

    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500, "500 오류 발생");
    }
}
