package br.com.backend.exceptions.handler;

import br.com.backend.exceptions.custom.*;
import br.com.backend.exceptions.models.StandartError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String ERRO_REQUISICAO = "Ocorreu um erro durante uma tentativa de requisição: {}";

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<StandartError> handleGlobal(HttpServletRequest req,
                                                      Exception exception) {
        StandartError standartError = StandartError.builder()
                .localDateTime(LocalDateTime.now())
                .status(500)
                .error(exception.getMessage())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(standartError);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<StandartError> invalidRequestException(HttpServletRequest req,
                                                                 InvalidRequestException exception) {
        StandartError standartError = StandartError.builder()
                .localDateTime(LocalDateTime.now())
                .status(400)
                .error(exception.getMessage())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standartError);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<StandartError> internalErrorException(HttpServletRequest req,
                                                                InternalErrorException exception) {
        StandartError standartError = StandartError.builder()
                .localDateTime(LocalDateTime.now())
                .status(500)
                .error(exception.getMessage())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(standartError);
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
                .status(429)
                .error(exception.getMessage())
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(standartError);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  WebRequest request) {

        List<String> listaErros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        StandartError standartError = StandartError.builder()
                .localDateTime(LocalDateTime.now())
                .status(422)
                .error(listaErros.toString())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(standartError);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  WebRequest request) {
        log.error(ERRO_REQUISICAO, ex.getMessage());

        StandartError standartError = StandartError.builder()
                .localDateTime(LocalDateTime.now())
                .status(422)
                .error("O conteúdo enviado para o servidor é inválido. Revise os dados e tente novamente")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(standartError);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<StandartError> handleMethodArgumentTypeMismatchException(HttpServletRequest req,
                                                                                   MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {

        log.error(ERRO_REQUISICAO, methodArgumentTypeMismatchException.getMessage());

        StandartError standartError = StandartError.builder()
                .localDateTime(LocalDateTime.now())
                .status(422)
                .error("O conteúdo enviado para o servidor é inválido. Revise os dados e tente novamente")
                .path(req.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(standartError);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException missingPathVariableException,
                                                               @NonNull HttpHeaders headers,
                                                               @NonNull HttpStatus status,
                                                               WebRequest request) {
        log.error(ERRO_REQUISICAO, missingPathVariableException.getMessage());

        StandartError standartError = StandartError.builder()
                .localDateTime(LocalDateTime.now())
                .status(422)
                .error("Uma ou mais variáveis obrigatórias não foram detectadas na URI")
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(standartError);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<StandartError> handleMaxUploadSizeExceededException(HttpServletRequest req,
                                                                              MaxUploadSizeExceededException exception) {
        log.error(ERRO_REQUISICAO, exception.getMessage());

        StandartError standartError = StandartError.builder()
                .localDateTime(LocalDateTime.now())
                .status(413)
                .error("Tamanho máximo permitido para upload de arquivos excedido")
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standartError);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandartError> handleDataIntegrityViolationException(HttpServletRequest req,
                                                                               DataIntegrityViolationException exception) {

        log.error("Ocorreu um erro de conflito no SQL durante uma tentativa de requisição: {}",
                exception.getMessage());

        StandartError standartError = StandartError.builder()
                .localDateTime(LocalDateTime.now())
                .status(500)
                .error("Ocorreu um erro interno de integridade de dados")
                .path(req.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(standartError);
    }

}
