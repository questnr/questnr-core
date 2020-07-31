package com.questnr.model.dto;

import com.questnr.model.dto.user.UserOtherDTO;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class CommentActionDTO {
    private Long commentActionId;

    private String commentObject;

    private List<MediaDTO> commentMediaList;

    private UserOtherDTO userActorDTO;

    private Set<ChildCommentActionDTO> childCommentDTOSet;

    private boolean childComment;

    private Date createdAt;

    private MetaDataDTO metaData;

    private CommentActionMetaDTO commentActionMeta;

    public Long getCommentActionId() {
        return commentActionId;
    }

    public void setCommentActionId(Long commentActionId) {
        this.commentActionId = commentActionId;
    }

    public String getCommentObject() {
        return commentObject;
    }

    public void setCommentObject(String commentObject) {
        this.commentObject = commentObject;
    }

    public List<MediaDTO> getCommentMediaList() {
        return commentMediaList;
    }

    public void setCommentMediaList(List<MediaDTO> commentMediaList) {
        this.commentMediaList = commentMediaList;
    }

    public UserOtherDTO getUserActorDTO() {
        return userActorDTO;
    }

    public void setUserActorDTO(UserOtherDTO userActorDTO) {
        this.userActorDTO = userActorDTO;
    }

    public Set<ChildCommentActionDTO> getChildCommentDTOSet() {
        return childCommentDTOSet;
    }

    public void setChildCommentDTOSet(Set<ChildCommentActionDTO> childCommentDTOSet) {
        this.childCommentDTOSet = childCommentDTOSet;
    }

    public boolean isChildComment() {
        return childComment;
    }

    public void setChildComment(boolean childComment) {
        this.childComment = childComment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public MetaDataDTO getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaDataDTO metaData) {
        this.metaData = metaData;
    }

    public CommentActionMetaDTO getCommentActionMeta() {
        return commentActionMeta;
    }

    public void setCommentActionMeta(CommentActionMetaDTO commentActionMeta) {
        this.commentActionMeta = commentActionMeta;
    }
}
