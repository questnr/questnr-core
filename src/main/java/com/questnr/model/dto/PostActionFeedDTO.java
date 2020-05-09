package com.questnr.model.dto;

import com.questnr.common.enums.PostActionType;

public class PostActionFeedDTO extends PostActionDTO {

    private CommunityForPostActionDTO communityDTO;
    private PostActionType postActionType;
    private UserDTO userDTO;
    private UserDTO userWhoShared;

    public CommunityForPostActionDTO getCommunityDTO() {
        return communityDTO;
    }

    public void setCommunityDTO(CommunityForPostActionDTO communityDTO) {
        this.communityDTO = communityDTO;
    }

    public PostActionType getPostActionType() {
        return postActionType;
    }

    public void setPostActionType(PostActionType postActionType) {
        this.postActionType = postActionType;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public UserDTO getUserWhoShared() {
        return userWhoShared;
    }

    public void setUserWhoShared(UserDTO userWhoShared) {
        this.userWhoShared = userWhoShared;
    }
}
