package dev.luizveronesi.autoconfigure.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final HttpStatus statusCode;

    private List<String> details;

    public ApplicationException(HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public ApplicationException(HttpStatus statusCode, String message, List<String> details) {
        this(statusCode, message);
        this.details = details;
    }

}
