package com.questnr.model.dto;

import com.questnr.model.entities.PostActionMetaInformation;

import java.util.ArrayList;
import java.util.List;

public class PostActionPublicDTO extends PostActionDTO {

    private UserOtherDTO userDTO;
    private CommunityForPostActionDTO communityDTO;
    private List<PostActionMetaInformation> metaList = new ArrayList<>();

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

    public List<PostActionMetaInformation> getMetaList() {
        return metaList;
    }

    public void setMetaList(List<PostActionMetaInformation> metaList) {
        this.metaList = metaList;
    }
}
