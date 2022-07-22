package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class ErrorPageController {

    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("404 에러 발생!");
        printErrorInfo(request);

        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        log.info("500 에러 발생!");
        printErrorInfo(request);

        return "error-page/500";
    }

    private void printErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION : ex = {}", request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE : {}", request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE : {}", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
        log.info("ERROR_REQUEST_URI : {}", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME : {}", request.getAttribute(RequestDispatcher.ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE : {}", request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
        log.info("request.getDispatcherType() = {}", request.getDispatcherType());
    }
}
