package com.questnr.model.dto.post.normal;

import com.questnr.model.dto.community.CommunityForPostActionDTO;
import com.questnr.model.dto.user.UserOtherDTO;

public class PostActionFeedDTO extends PostActionDTO {

    private CommunityForPostActionDTO communityDTO;
    private UserOtherDTO userDTO;
    private UserOtherDTO userWhoShared;

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

    public UserOtherDTO getUserWhoShared() {
        return userWhoShared;
    }

    public void setUserWhoShared(UserOtherDTO userWhoShared) {
        this.userWhoShared = userWhoShared;
    }
}
