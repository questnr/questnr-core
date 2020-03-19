package com.questnr.model.dto;

import com.questnr.services.AmazonS3Client;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDTO {

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    UserCommonService userCommonService;

    private String userName;

    private String firstName;

    private String lastName;

    private String fullName;

    private String emailId;

    private String avatar;

    private String avatarLink;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarLink() {
        if(this.getAvatar() == null || this.getAvatar().trim().isEmpty()){
            return null;
        }
        return this.amazonS3Client.getS3BucketUrl(userCommonService.joinPathToFile(this.getAvatar()));
    }
}
