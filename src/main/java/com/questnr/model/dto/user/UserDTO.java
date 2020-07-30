package com.questnr.model.dto.user;

import com.questnr.model.dto.AvatarDTO;
import com.questnr.services.CommonService;

public class UserDTO {

    private Long userId;

    private String username;

    private String displayName;

    private String firstName;

    private String lastName;

    private String bio;

    private String slug;

    private AvatarDTO avatarDTO;

    private AvatarDTO banner;

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

    public String getDisplayName() {
        if (!CommonService.isNull(this.getFirstName()) && !CommonService.isNull(this.getLastName())) {
            return this.getFirstName() + " " + this.getLastName();
        }
        return this.getUsername();
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

    public AvatarDTO getBanner() {
        return banner;
    }

    public void setBanner(AvatarDTO banner) {
        this.banner = banner;
    }

    public UserMetaDTO getUserMeta() {
        return userMeta;
    }

    public void setUserMeta(UserMetaDTO userMeta) {
        this.userMeta = userMeta;
    }
}
