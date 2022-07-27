package hello.exception.api;

import hello.exception.BadRequestException;
import hello.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionController {

    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }

        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        return new MemberDto(id, "hello " + id);
    }

    /**
     * 스프링부트가 기본으로 제공하는 HandlerExceptionResolver
     * 1. ResponseStatusExceptionResolver
     * 2. DefaultHandlerExceptionResolver
     * 3. ExceptionHandlerExceptionResolver -> ApiExceptionV2Controller
     */

    /**
     * 1. ResponseStatusExceptionResolver
     *  1) @ResponseStatus 붙어있는 Exception 처리
     *  2) ResponseStatusException 처리
     */
    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1() {
        throw new BadRequestException();
    }

    @GetMapping("/api/response-status-ex2")
    public String responseStatusEx2() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청", new IllegalArgumentException());
    }

    /**
     * 2. DefaultHandlerExceptionResolver
     * 스프링 내부에서 발생하는 스프링 예외 처리
     *  ex) TypeMismatchException
     */
    @GetMapping("/api/default-handler-ex")
    public String defaultException(@RequestParam Integer data) {    // 파라미터로 문자 전송
        return "ok";
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {

        private String memberId;
        private String name;
    }
}
