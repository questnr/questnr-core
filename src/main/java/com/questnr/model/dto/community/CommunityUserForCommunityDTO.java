package com.questnr.model.dto.community;

import com.questnr.model.dto.user.UserOtherDTO;

public class CommunityUserForCommunityDTO {

    private UserOtherDTO communityUser;

    public UserOtherDTO getCommunityUser() {
        return communityUser;
    }

    public void setCommunityUser(UserOtherDTO communityUser) {
        this.communityUser = communityUser;
    }
}
