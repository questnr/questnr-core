package com.questnr.services.community;

import com.questnr.exceptions.AccessException;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostMedia;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.services.CommonService;
import com.questnr.services.user.UserCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccessService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    CommonService commonService;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    UserCommonService userCommonService;

    public boolean hasAccessToCommunity(Long communityId) throws AccessException {
        User user = userCommonService.getUser();
        Community community = communityCommonService.getCommunity(communityId);
        List<Long> userIdList = community.getUsers().stream().map(communityUser ->
                communityUser.getUser().getUserId()
        ).collect(Collectors.toList());
        if (userIdList.contains(user.getUserId()))
            return true;
        throw new AccessException("You don't have access to this community");
    }

    public boolean hasAccessToPost(Long postId) throws AccessException {
        PostAction postAction = postActionRepository.findByPostActionId(postId);
        Long communityId = commonService.getCommunityId(postAction);
        try {
            if (communityId != null && this.hasAccessToCommunity(communityId))
                return true;
        } catch (Exception e) {
            throw new AccessException("You don't have access to this post");
        }
        return false;
    }

    public boolean hasAccessToCommunityAvatar(Long communityId) throws AccessException {
        try {
            return this.hasAccessToCommunity(communityId);
        }catch (Exception e){
            throw new AccessException("You don't have access to this community");
        }
    }
}
