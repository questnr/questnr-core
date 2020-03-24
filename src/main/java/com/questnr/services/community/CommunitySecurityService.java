package com.questnr.services.community;

import com.questnr.exceptions.AccessException;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.User;
import com.questnr.services.user.UserCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunitySecurityService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    UserCommonService userCommonService;

    public boolean hasAccessToCommunity(Long communityId){
        User user = userCommonService.getUser();
        Community community = communityCommonService.getCommunity(communityId);
        List<Long> userIdList = community.getUsers().stream().map(communityUser ->
                communityUser.getUser().getUserId()
        ).collect(Collectors.toList());
        if(userIdList.contains(user.getUserId())){
            return true;
        }
        throw new AccessException("You don't have access to post in this community");
    }
}
