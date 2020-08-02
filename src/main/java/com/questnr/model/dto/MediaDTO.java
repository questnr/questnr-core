package com.questnr.model.dto;

import com.questnr.common.enums.ResourceType;

public class MediaDTO {
    private String postMediaLink;

    private ResourceType resourceType;

    private String fileExtension;

    public String getPostMediaLink() {
        return postMediaLink;
    }

    public void setPostMediaLink(String postMediaLink) {
        this.postMediaLink = postMediaLink;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
