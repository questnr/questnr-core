package com.questnr.model.dto;

public class PostActionForCommunityDTO extends PostActionDTO {
    private UserOtherDTO userDTO;

    public UserOtherDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserOtherDTO userDTO) {
        this.userDTO = userDTO;
    }
}
