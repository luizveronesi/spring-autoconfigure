package dev.luizveronesi.autoconfigure.exception;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@ControllerAdvice
public class ApplicationExceptionHandler {

    public static final String HANDLING_TYPE_MESSAGE = "Handling {} - Type: {}, Message: {}";

    public static final String STACKTRACE = "StackTrace";

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<DocumentationErrorResponse> handleAccessDeniedException(Exception e) {
        log.info(HANDLING_TYPE_MESSAGE, "AccessDeniedException", e.getClass().getName(), ExceptionUtils.getMessage(e));
        DocumentationErrorResponse err = DocumentationErrorResponse.builder()
                .description(HttpStatus.FORBIDDEN.getReasonPhrase())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<DocumentationErrorResponse> handleException(Exception e) {
        log.info(HANDLING_TYPE_MESSAGE, "Exception", e.getClass().getName(), ExceptionUtils.getStackTrace(e));
        var err = DocumentationErrorResponse.builder()
                .description(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<DocumentationErrorResponse> handleConstraintViolationException(
            ConstraintViolationException e) {
        log.info(HANDLING_TYPE_MESSAGE, "ConstraintViolationException", e.getClass().getName(), e.getMessage());
        log.debug(STACKTRACE, e);
        var constraintsViolated = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        DocumentationErrorResponse err = DocumentationErrorResponse.builder()
                .description(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .details(constraintsViolated)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(ApplicationException.class)
    @ResponseBody
    public ResponseEntity<DocumentationErrorResponse> handleApplicationException(ApplicationException e) {
        log.info(HANDLING_TYPE_MESSAGE, "ApplicationException", e.getClass().getName(), e.getMessage());
        log.debug(STACKTRACE, e);
        var err = DocumentationErrorResponse.builder()
                .description(e.getMessage())
                .details(e.getDetails())
                .build();

        return ResponseEntity.status(e.getStatusCode()).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<DocumentationErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.info(HANDLING_TYPE_MESSAGE, "MethodArgumentNotValidException", e.getClass().getName(), e.getMessage());
        log.debug(STACKTRACE, e);
        var constraintsViolated = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " - " + error.getDefaultMessage())
                .collect(Collectors.toList());

        var err = DocumentationErrorResponse.builder()
                .description(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .details(constraintsViolated)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<DocumentationErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        log.info(HANDLING_TYPE_MESSAGE, "MissingServletRequestParameterException", e.getClass().getName(),
                e.getMessage());
        log.debug(STACKTRACE, e);
        var err = DocumentationErrorResponse.builder()
                .description(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .details(Optional.ofNullable(e.getMessage())
                        .map(Collections::singletonList)
                        .orElseGet(Collections::emptyList))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<DocumentationErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        log.info(HANDLING_TYPE_MESSAGE, "HttpMessageNotReadableException", e.getClass().getName(), e.getMessage());
        log.debug(STACKTRACE, e);
        var err = DocumentationErrorResponse.builder()
                .description(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .details(e.getMessage() != null ? Collections.singletonList(e.getMessage()) : Collections.emptyList())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<DocumentationErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        log.info(HANDLING_TYPE_MESSAGE, "MethodArgumentTypeMismatchException", e.getClass().getName(), e.getMessage());
        log.debug(STACKTRACE, e);
        var err = DocumentationErrorResponse.builder()
                .description(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .details(e.getMessage() != null ? Collections.singletonList(e.getMessage()) : Collections.emptyList())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

}
