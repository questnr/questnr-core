package com.questnr.model.dto;

import com.questnr.common.enums.MediaType;

public class PostMediaDTO {
    private String postMediaLink;

    private MediaType mediaType;

    public String getPostMediaLink() {
        return postMediaLink;
    }

    public void setPostMediaLink(String postMediaLink) {
        this.postMediaLink = postMediaLink;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }
}
