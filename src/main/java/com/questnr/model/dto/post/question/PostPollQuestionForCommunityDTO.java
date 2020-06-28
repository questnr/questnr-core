package com.questnr.model.dto.post.question;

import com.questnr.model.dto.user.UserOtherDTO;

public class PostPollQuestionForCommunityDTO extends PostPollQuestionDTO {

    private UserOtherDTO userDTO;

    public UserOtherDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserOtherDTO userDTO) {
        this.userDTO = userDTO;
    }
}
