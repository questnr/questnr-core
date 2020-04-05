package com.questnr.model.dto;

public class ChildCommentActionDTO {
    private Long commentActionId;

    private String commentObject;

    private UserDTO userActorDTO;

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

    public UserDTO getUserActorDTO() {
        return userActorDTO;
    }

    public void setUserActorDTO(UserDTO userActorDTO) {
        this.userActorDTO = userActorDTO;
    }
}
