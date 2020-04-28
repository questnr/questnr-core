package com.questnr.responses;

import com.questnr.common.enums.MediaType;

public class AvatarStorageData {
    String url;
    String key;
    MediaType mediaType;

    public AvatarStorageData() {

    }

    public AvatarStorageData(String url, String fileName) {
        this.url = url;
        this.key = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }
}
