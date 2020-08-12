package com.questnr.requests;

import com.questnr.common.enums.CommunityPrivacy;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;

public class CommunityRequest {
    public String communityName;

    private String description;

    private MultipartFile[] avatarFile;

    private CommunityPrivacy communityPrivacy;

    private String communityTags;

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

    public CommunityPrivacy getCommunityPrivacy() {
        return communityPrivacy;
    }

    public void setCommunityPrivacy(CommunityPrivacy communityPrivacy) {
        this.communityPrivacy = communityPrivacy;
    }

    public String getCommunityTags() {
        return communityTags;
    }

    public void setCommunityTags(String communityTags) {
        this.communityTags = communityTags;
    }
}
