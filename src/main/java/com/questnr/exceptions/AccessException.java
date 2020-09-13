package com.questnr.exceptions;


import com.questnr.common.message.helper.Message;
import com.questnr.common.message.helper.messages.CommonMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class AccessException extends RuntimeException {
    public AccessException() {
        super(CommonMessages.C100.getText());
    }

    public AccessException(String message) {
        super(message);
    }

    public AccessException(Message message) {
        super(message.getText());
    }

    public AccessException(String message, Throwable cause) {
        super(message, cause);
    }
}