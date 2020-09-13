package com.questnr.exceptions;


import com.questnr.common.message.helper.Message;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(Message message) {
        super(message.getText());
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}