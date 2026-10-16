package haewon.nfc.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(
            ResponseStatusException exception
    ) {
        HttpStatus status = HttpStatus.resolve(
                exception.getStatusCode().value()
        );

        String code = "REQUEST_ERROR";

        if (status == HttpStatus.NOT_FOUND) {
            code = "CARD_NOT_FOUND";
        }

        String message = exception.getReason();

        if (message == null || message.isBlank()) {
            message = "요청을 처리할 수 없습니다.";
        }

        Map<String, Object> body = Map.of(
                "success", false,
                "code", code,
                "message", message
        );

        return ResponseEntity
                .status(exception.getStatusCode())
                .body(body);
    }
}