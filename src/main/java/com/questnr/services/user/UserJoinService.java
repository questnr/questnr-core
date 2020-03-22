package com.questnr.services.user;

import com.questnr.model.entities.Community;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.services.community.CommunityCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserJoinService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserRepository userRepository;

    public List<Community> getCommunityJoinedList() {
        List<Community> communities = userCommonService.getUser().getCommunityJoinedList().stream().map(communityUser ->
                communityUser.getCommunity()
        ).collect(Collectors.toList());
        return communities;
    }

    public List<Community> getCommunityInvitationList() {
        List<Community> communities = userCommonService.getUser().getCommunityInvitedUsers().stream().map(communityUser ->
                communityUser.getCommunity()
        ).collect(Collectors.toList());
        return communities;
    }
}
