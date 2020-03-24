package com.questnr.responses;

public class AvatarStorageData {
    String url;
    String key;

    public AvatarStorageData(){

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
}
