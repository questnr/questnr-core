package com.questnr.model.dto.user;

import com.questnr.model.dto.AvatarDTO;

public class UserDTO {

    private Long userId;

    private String username;

    private String firstName;

    private String lastName;

    private String bio;

    private String slug;

    private AvatarDTO avatarDTO;

    private UserMetaDTO userMeta;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public AvatarDTO getAvatarDTO() {
        return avatarDTO;
    }

    public void setAvatarDTO(AvatarDTO avatarDTO) {
        this.avatarDTO = avatarDTO;
    }

    public UserMetaDTO getUserMeta() {
        return userMeta;
    }

    public void setUserMeta(UserMetaDTO userMeta) {
        this.userMeta = userMeta;
    }
}
