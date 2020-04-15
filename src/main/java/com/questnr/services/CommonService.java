package com.questnr.services;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityUser;
import com.questnr.model.entities.PostAction;
import com.questnr.responses.TimeData;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<Community> getCommunityList(Set<CommunityUser> communityUserList){
        return communityUserList.stream().map(CommunityUser::getCommunity).collect(Collectors.toList());
    }

    /*
     * Get current server date & time
     */
    public static String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        return format.format(new Date());
    }

    public static TimeData calculateTimeFromSeconds(Long elapsed){
        Integer hours = (int) Math.floor(elapsed / 3600);

        Integer minutes = (int) Math.floor((elapsed - hours * 3600) / 60);

        Integer seconds = (int) Math.floor(elapsed - hours * 3600 - minutes * 60);

        return new TimeData(hours, minutes, seconds);
    }
}