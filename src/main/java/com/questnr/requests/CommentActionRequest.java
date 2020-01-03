package com.questnr.requests;

public class CommentActionRequest {
    Long postId;
    Long parentCommentId;
    String commentObject;

    public CommentActionRequest() {
    }

    public CommentActionRequest(String commentObject) {
        this.commentObject = commentObject;
    }

    public CommentActionRequest(Long postId, String commentObject) {
        this.postId = postId;
        this.commentObject = commentObject;
    }

    public CommentActionRequest(Long postId, Long parentCommentId, String commentObject) {
        this.postId = postId;
        this.parentCommentId = parentCommentId;
        this.commentObject = commentObject;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public String getCommentObject() {
        return commentObject;
    }

    public void setCommentObject(String commentObject) {
        this.commentObject = commentObject;
    }
}
