package com.questnr.model.dto;

import com.questnr.common.enums.ResourceType;

public class PostMediaDTO {
    private String postMediaLink;

    private ResourceType resourceType;

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
}
