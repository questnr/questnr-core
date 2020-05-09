package com.questnr.model.dto;

public class PostActionForCommunityDTO extends PostActionDTO {
    private UserDTO userDTO;

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
