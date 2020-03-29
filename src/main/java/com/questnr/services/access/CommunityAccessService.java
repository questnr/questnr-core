package com.questnr.services.access;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.User;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommunityAccessService {
    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    UserCommonService userCommonService;

    public boolean isUserOwnerOfCommunity(User user, Community community) {
        if (Objects.equals(user.getUserId(), community.getOwnerUser().getUserId())) {
            return true;
        }
        return false;
    }

    public boolean isUserMemberOfCommunity(User user, Community community){
        // If user is the owner of the community
        if (this.isUserOwnerOfCommunity(user, community)) {
            return true;
        }
        // If the user is a member of the community
        List<Long> userIdList = community.getUsers().stream().map(communityUser ->
                communityUser.getUser().getUserId()
        ).collect(Collectors.toList());
        if (userIdList.contains(user.getUserId()))
            return true;
        return false;
    }
}
