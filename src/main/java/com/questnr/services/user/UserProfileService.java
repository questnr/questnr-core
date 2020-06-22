package com.questnr.services.user;

import com.questnr.common.enums.PostType;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.model.repositories.CommunityUserRepository;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.UserFollowerRepository;
import com.questnr.responses.UserMetaProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    CommunityUserRepository communityUserRepository;

    @Autowired
    UserFollowerRepository userFollowerRepository;

    public UserMetaProfileResponse getUserProfileDetails(String userSlug) {
        User user = userCommonService.getUserByUserSlug(userSlug);
        UserMetaProfileResponse userMetaProfileResponse = new UserMetaProfileResponse();
        userMetaProfileResponse.setFollowingTo(userFollowerRepository.countByFollowingUser(user));
        userMetaProfileResponse.setFollowers(userFollowerRepository.countByUser(user));
        userMetaProfileResponse.setPostsOnProfile(postActionRepository.countByUserActorAndCommunity(user, null));
        userMetaProfileResponse.setPosts(postActionRepository.countByUserActor(user));
        userMetaProfileResponse.setPostsOnCommunities(userMetaProfileResponse.getPosts() - userMetaProfileResponse.getPostsOnProfile());
        userMetaProfileResponse.setTotalQuestions(postActionRepository.countAllByUserActorAndPostType(user, PostType.question));
        userMetaProfileResponse.setFollowsCommunities(communityUserRepository.countByUser(user));
        userMetaProfileResponse.setOwnsCommunities(communityRepository.countByOwnerUser(user));
        return userMetaProfileResponse;
    }
}
