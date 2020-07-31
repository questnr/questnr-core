package com.questnr.model.dto.post.normal;

import com.questnr.model.dto.community.CommunityForPostActionDTO;
import com.questnr.model.dto.MediaDTO;
import com.questnr.model.dto.user.UserOtherDTO;

import java.util.List;

public class PostActionForMediaDTO {
    private Long postActionId;
    private UserOtherDTO userDTO;
    private CommunityForPostActionDTO communityDTO;
    private List<MediaDTO> postMediaList;

    public UserOtherDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserOtherDTO userDTO) {
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

    public List<MediaDTO> getPostMediaList() {
        return postMediaList;
    }

    public void setPostMediaList(List<MediaDTO> postMediaList) {
        this.postMediaList = postMediaList;
    }
}
