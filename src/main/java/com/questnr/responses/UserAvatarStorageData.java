package com.questnr.responses;

public class UserAvatarStorageData {
    String url;
    String fileName;

    public UserAvatarStorageData(){

    }

    public UserAvatarStorageData(String url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
