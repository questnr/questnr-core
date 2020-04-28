package com.questnr.model.dto;

import java.util.List;

public class PostActionForMediaDTO {
    private Long postActionId;
    private UserDTO userDTO;
    private CommunityForPostActionDTO communityDTO;
    private List<PostMediaDTO> postMediaList;

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public CommunityForPostActionDTO getCommunityDTO() {
        return communityDTO;
    }

    public void setCommunityDTO(CommunityForPostActionDTO communityDTO) {
        this.communityDTO = communityDTO;
    }

    public Long getPostActionId() {
        return postActionId;
    }

    public void setPostActionId(Long postActionId) {
        this.postActionId = postActionId;
    }

    public List<PostMediaDTO> getPostMediaList() {
        return postMediaList;
    }

    public void setPostMediaList(List<PostMediaDTO> postMediaList) {
        this.postMediaList = postMediaList;
    }
}
