package hello.exception.exhandler.advice;

import hello.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
//@RestControllerAdvice(annotations = RestController.class)
//@RestControllerAdvice("hello.exception.exhandler.advice")
//@RestControllerAdvice(assignableTypes = {ApiExceptionV2Controller.class, ApiExceptionV3Controller.class})
public class ExControllerAdvice {

    /**
     * 대상으로 지정한 여러 컨트롤러에 대해 ExceptionHandlerExceptionResolver를 적용할 수 있음!
     * 지정 방법
     * 1. 어노테이션
     * 2. 패키지
     * 3. 클래스
     * -> 대상을 지정하지 않으면 모든 컨트롤러에 적용
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
}
