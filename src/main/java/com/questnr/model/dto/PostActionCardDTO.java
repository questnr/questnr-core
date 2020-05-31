package com.questnr.model.dto;

import com.questnr.common.enums.PostActionType;

public class PostActionCardDTO extends PostActionDTO {

    private CommunityForPostActionDTO communityDTO;
    private UserOtherDTO userDTO;
    private PostActionType postActionType;
    private UserOtherDTO sharedPostOwner;

    public CommunityForPostActionDTO getCommunityDTO() {
        return communityDTO;
    }

    public void setCommunityDTO(CommunityForPostActionDTO communityDTO) {
        this.communityDTO = communityDTO;
    }

    public UserOtherDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserOtherDTO userDTO) {
        this.userDTO = userDTO;
    }

    public PostActionType getPostActionType() {
        return postActionType;
    }

    public void setPostActionType(PostActionType postActionType) {
        this.postActionType = postActionType;
    }

    public UserOtherDTO getSharedPostOwner() {
        return sharedPostOwner;
    }

    public void setSharedPostOwner(UserOtherDTO sharedPostOwner) {
        this.sharedPostOwner = sharedPostOwner;
    }
}