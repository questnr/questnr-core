package com.questnr.requests;

import com.questnr.common.enums.CommunityPrivacy;

public class CommunityPrivacyUpdateRequest {
   private CommunityPrivacy communityPrivacy;

    public CommunityPrivacy getCommunityPrivacy() {
        return communityPrivacy;
    }

    public void setCommunityPrivacy(CommunityPrivacy communityPrivacy) {
        this.communityPrivacy = communityPrivacy;
    }
}
