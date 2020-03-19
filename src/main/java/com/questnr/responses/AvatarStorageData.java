package com.questnr.responses;

public class AvatarStorageData {
    String url;
    String fileName;

    public AvatarStorageData(){

    }

    public AvatarStorageData(String url, String fileName) {
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
