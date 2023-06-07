package br.com.backend.services.exceptions;

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

}
