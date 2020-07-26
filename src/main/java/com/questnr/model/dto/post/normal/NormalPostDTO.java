package com.questnr.model.dto.post.normal;

import com.questnr.common.enums.PostEditorType;

public class NormalPostDTO {
    private String text;
    private String blogTitle;
    private PostEditorType postEditorType;

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
        this.blogTitle = blogTitle;
    }

    public PostEditorType getPostEditorType() {
        return postEditorType;
    }

    public void setPostEditorType(PostEditorType postEditorType) {
        this.postEditorType = postEditorType;
    }
}
