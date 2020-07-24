package com.questnr.model.dto.post.normal;

import com.questnr.common.enums.PostEditorType;
import com.questnr.common.enums.PublishStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class PostActionRequestDTO {

    private Long postId;
    private String text;
    List<MultipartFile> files;
    private PublishStatus status = PublishStatus.publish;
    private PostEditorType postEditorType = PostEditorType.normal;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

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

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }

    public PostEditorType getPostEditorType() {
        return postEditorType;
    }

    public void setPostEditorType(PostEditorType postEditorType) {
        this.postEditorType = postEditorType;
    }
}
