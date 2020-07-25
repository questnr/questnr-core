package com.questnr.model.dto.post.normal;

import com.questnr.common.enums.PublishStatus;

public class PostActionUpdateRequestDTO {

    private String text;
    private String blogTitle;
    private PublishStatus status;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle.trim();
    }

    public PublishStatus getStatus() {
        return status;
    }

    public void setStatus(PublishStatus status) {
        this.status = status;
    }
}
