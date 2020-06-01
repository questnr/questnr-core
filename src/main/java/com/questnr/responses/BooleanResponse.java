package com.questnr.responses;

public class BooleanResponse {
    private Boolean status = false;
    private String errorMessage;

    public BooleanResponse(){

    }

    public BooleanResponse(Boolean status) {
        this.status = status;
    }

    public BooleanResponse(Boolean status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
