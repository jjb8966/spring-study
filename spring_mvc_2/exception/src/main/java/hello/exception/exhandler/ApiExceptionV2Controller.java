package hello.exception.exhandler;

import hello.exception.UserException;
import hello.exception.api.ApiExceptionController;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

    /**
     * 해당 컨트롤러에서 예외가 발생할 시
     * 발생한 예외에 대한 @ExceptionHandler 애노테이션이 붙은 메소드가 있으면
     * 메소드가 호출되어 예외를 처리함
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)     // 여기서 HTTP 상태코드 지정
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);

        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler   // 예외 생략 시 파라미터의 예외가 지정됨
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);

        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);   // 여기서 HTTP 상태코드 지정
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandler] ex", e);

        return new ErrorResult("Ex", "내부 오류");
    }

    @GetMapping("/api2/members/{id}")
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

    @Data
    @AllArgsConstructor
    static class MemberDto {

        private String memberId;
        private String name;
    }
}
