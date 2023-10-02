package br.com.backend.exceptions;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<StandartError> invalidRequestException(HttpServletRequest req, InvalidRequestException exception) {
        StandartError standartError = StandartError.builder()
                .localDateTime(LocalDateTime.now())
                .status(400)
                .error(exception.getMessage())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standartError);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<StandartError> feignConnectionException(HttpServletRequest req, FeignConnectionException exception) {
        StandartError standartError = StandartError.builder()
                .localDateTime(LocalDateTime.now())
                .status(400)
                .error(exception.getMessage())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standartError);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<StandartError> objectNotFoundException(HttpServletRequest req, ObjectNotFoundException exception) {
        StandartError standartError = StandartError.builder()
                .localDateTime(LocalDateTime.now())
                .status(404)
                .error(exception.getMessage())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standartError);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<StandartError> unauthorizedAccesException(HttpServletRequest req, UnauthorizedAccessException exception) {
        StandartError standartError = StandartError.builder()
                .localDateTime(LocalDateTime.now())
                .status(403)
                .error(exception.getMessage())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(standartError);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<StandartError> tooManyRequestsException(HttpServletRequest req, TooManyRequestsException exception) {
        StandartError standartError = StandartError.builder()
                .localDateTime(LocalDateTime.now())
                .status(409)
                .error(exception.getMessage())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(standartError);
    }

}
