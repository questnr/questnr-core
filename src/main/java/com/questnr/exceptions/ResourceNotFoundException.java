package com.questnr.exceptions;


import com.questnr.common.message.helper.Message;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(Message message) {
        super(message.getText());
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}