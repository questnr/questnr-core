package com.questnr.requests;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;

public class CommunityRequest {
    public String communityName;

    private String description;

    private MultipartFile[] avatarFile;

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

    @Nullable
    public MultipartFile[] getAvatarFile() {
        return avatarFile;
    }

    public void setAvatarFile(@Nullable MultipartFile[] avatarFile) {
        this.avatarFile = avatarFile;
    }
}
