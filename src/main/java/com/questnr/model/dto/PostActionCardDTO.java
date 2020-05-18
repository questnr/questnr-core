package com.questnr.model.dto;

import com.questnr.common.enums.PostActionType;

public class PostActionCardDTO extends PostActionDTO {

    private CommunityForPostActionDTO communityDTO;
    private UserDTO userDTO;
    private PostActionType postActionType;
    private UserDTO sharedPostOwner;

    public CommunityForPostActionDTO getCommunityDTO() {
        return communityDTO;
    }

    public void setCommunityDTO(CommunityForPostActionDTO communityDTO) {
        this.communityDTO = communityDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public PostActionType getPostActionType() {
        return postActionType;
    }

    public void setPostActionType(PostActionType postActionType) {
        this.postActionType = postActionType;
    }

    public UserDTO getSharedPostOwner() {
        return sharedPostOwner;
    }

    public void setSharedPostOwner(UserDTO sharedPostOwner) {
        this.sharedPostOwner = sharedPostOwner;
    }
}