package com.questnr.responses;

public class AvatarResponse {
    private String avatarLink;

    public AvatarResponse(){

    }

    public AvatarResponse(String avatarLink){
        this.avatarLink = avatarLink;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }
}
