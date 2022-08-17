package hello.typeconverter.controller;

import hello.typeconverter.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class HelloController {

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {
        // 직접 타입 변환
        String data = request.getParameter("data");
        int number = Integer.parseInt(data);
        log.info("number = {}", number);

        return "ok";
    }

    @GetMapping("/hello-v2")
    public String helloV1(@RequestParam("data") Integer number) {
        // @RequestParam 매개변수로 받을 때 Integer로 변환해서 받음
        // -> 스프링 타입 컨버터가 작동함!!
        log.info("number = {}", number);

        return "ok";
    }

    @GetMapping("/ip-port")
    // ArgumentResolver에서 ConversionService를 사용해 타입을 변환함
    public String ipPort(@ModelAttribute IpPort ipPort) {
        log.info("String -> IpPort : {}", ipPort);
        return "ok";
    }
}
