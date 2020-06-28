package com.questnr.model.dto.post.question;

import com.questnr.model.dto.community.CommunityForPostActionDTO;
import com.questnr.model.dto.user.UserOtherDTO;

public class PostPollQuestionFeedDTO extends PostPollQuestionDTO {

    private CommunityForPostActionDTO communityDTO;
    private UserOtherDTO userDTO;

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
}
