package com.questnr.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DoesNotExistsException extends RuntimeException {
    public DoesNotExistsException() {
        super();
    }

    public DoesNotExistsException(String message) {
        super(message);
    }

    public DoesNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}