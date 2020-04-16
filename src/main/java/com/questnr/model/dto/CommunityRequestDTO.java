package com.questnr.model.dto;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;

public class CommunityRequestDTO {
    public String communityName;

    private String description;

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

}
