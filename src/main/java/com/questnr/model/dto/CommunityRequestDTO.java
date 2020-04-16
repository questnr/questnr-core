package com.questnr.model.dto;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;

public class CommunityRequestDTO {
    public String communityName;

    private String description;

    @Nullable
    private MultipartFile avatar;

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }
}
