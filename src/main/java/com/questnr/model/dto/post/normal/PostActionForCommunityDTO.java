package com.questnr.model.dto.post.normal;

import com.questnr.model.dto.user.UserOtherDTO;

public class PostActionForCommunityDTO extends PostActionDTO {
    private UserOtherDTO userDTO;

    public UserOtherDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserOtherDTO userDTO) {
        this.userDTO = userDTO;
    }
}
