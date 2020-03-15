package com.questnr.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Media not found!")
public class AmazonS3APIError extends RuntimeException {
    String localizedMessage;
    public AmazonS3APIError() {
        super();
    }

    public AmazonS3APIError(String message, String localizedMessage) {
        super(message);
        this.localizedMessage = localizedMessage;
    }

    public AmazonS3APIError(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getLocalizedMessage() {
        return localizedMessage;
    }

    public void setLocalizedMessage(String localizedMessage) {
        this.localizedMessage = localizedMessage;
    }
}