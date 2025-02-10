package isol.tcat_api.global.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    protected ResponseEntity<ErrorResponse> handleGlobalException(GlobalException e) {
        log.error(String.valueOf(e));
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(ExtendedGlobalException.class)
    protected ResponseEntity<ErrorResponse> handleExtendedGlobalException(ExtendedGlobalException e) {
        log.error(String.valueOf(e));
        return ErrorResponse.toResponseEntity(e.getErrorCode(), e.getData());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundException(NoHandlerFoundException e) {
        log.error(String.valueOf(e));
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .code(HttpStatus.NOT_FOUND.value())
                .message("잘못된 경로입니다.")
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(String.valueOf(e));
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(400)
                .code(4001)
                .message("잘못된 양식의 데이터입니다. ")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleOtherExceptions(Exception e) {
        log.error(String.valueOf(e));
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("서버에서 문제가 발생했습니다.")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
