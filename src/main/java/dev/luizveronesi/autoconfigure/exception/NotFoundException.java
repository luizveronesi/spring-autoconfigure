package dev.luizveronesi.autoconfigure.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApplicationException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public NotFoundException(String message, List<String> details) {
        super(HttpStatus.NOT_FOUND, message, details);
    }

}
