package com.questnr.services;

import com.questnr.model.entities.PostAction;
import com.questnr.responses.TimeData;
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

    public static TimeData calculateTimeFromSeconds(Long elapsed){
        Integer hours = (int) Math.floor(elapsed / 3600);

        Integer minutes = (int) Math.floor((elapsed - hours * 3600) / 60);

        Integer seconds = (int) Math.floor(elapsed - hours * 3600 - minutes * 60);

        return new TimeData(hours, minutes, seconds);
    }
}