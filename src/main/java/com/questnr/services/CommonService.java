package com.questnr.services;

import com.questnr.model.entities.PostAction;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
    public boolean isNull(String string) {
        return string == null || string.trim().isEmpty();
    }

    public Long getCommunityId(PostAction postAction) {
        if (postAction.getCommunity() != null && this.isNull(postAction.getCommunity().getCommunityId().toString())) {
            return postAction.getCommunity().getCommunityId();
        }
        return null;
    }

}