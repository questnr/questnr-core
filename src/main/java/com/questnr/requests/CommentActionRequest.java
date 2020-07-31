package com.questnr.requests;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class CommentActionRequest {
    private Long postId;
    private Long parentCommentId;
    private String commentObject;
    private List<MultipartFile> files;

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

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
}
