package hello.typeconverter.controller;

import hello.typeconverter.UserData;
import hello.typeconverter.type.IpPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("/hello-v1")
    public String helloV1(@RequestParam Integer data) {
        log.info("String -> Integer : {}", data);
        return "ok";
    }

    @GetMapping("/hello-v2")
    public String helloV2(@ModelAttribute UserData data) {
        log.info("String -> Object : {}", data);
        return "ok";
    }

    @GetMapping("/hello-v3/{data}")
    public String helloV3(@PathVariable String data) {
        log.info("String -> String : {}", data);
        return "ok";
    }

    @GetMapping("/ip-port")
    public String ipPort(@ModelAttribute IpPort ipPort) {
        log.info("String -> IpPort : {}", ipPort);
        log.info("ip : {}", ipPort.getIp());
        log.info("port : {}", ipPort.getPort());
        return "ok";
    }
}
