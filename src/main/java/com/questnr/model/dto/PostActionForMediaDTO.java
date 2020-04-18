package com.questnr.model.dto;

import java.util.List;

public class PostActionForMediaDTO {
    private Long postActionId;
    private UserDTO userDTO;
    private CommunityForPostActionDTO communityDTO;
    private List<PostMediaDTO> postMediaDTOList;

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

    public List<PostMediaDTO> getPostMediaDTOList() {
        return postMediaDTOList;
    }

    public void setPostMediaDTOList(List<PostMediaDTO> postMediaDTOList) {
        this.postMediaDTOList = postMediaDTOList;
    }
}
