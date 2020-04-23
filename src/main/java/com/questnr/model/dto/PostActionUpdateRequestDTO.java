package com.questnr.model.dto;

import com.questnr.common.enums.PublishStatus;

public class PostActionUpdateRequestDTO {

    private String text;
    private PublishStatus status;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PublishStatus getStatus() {
        return status;
    }

    public void setStatus(PublishStatus status) {
        this.status = status;
    }
}
