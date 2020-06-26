package com.questnr.services.user;

import com.questnr.common.enums.PostType;
import com.questnr.model.entities.Community;
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

    public UserMetaProfileResponse getUserProfileDetails(String userSlug, String params) {
        return this.getUserProfileDetails(userCommonService.getUserByUserSlug(userSlug), params);
    }

    public UserMetaProfileResponse getUserProfileDetails(User user, String params) {
        String[] paramArray = params.split(",");
        UserMetaProfileResponse userMetaProfileResponse = new UserMetaProfileResponse();
        for(String param: paramArray){
            this.setUserProfileProperty(user, userMetaProfileResponse, param.trim());
        }
        return userMetaProfileResponse;
    }

    public UserMetaProfileResponse setUserProfileProperty(User user,
                                                          UserMetaProfileResponse userMetaProfileResponse,
                                                                        String param){
        switch (param) {
            case "followers":
                userMetaProfileResponse.setFollowers(this.getUserFollowers(user));
                break;
            case "followingTo":
                userMetaProfileResponse.setFollowingTo(this.getUserBeingFollowed(user));
                break;
            case "posts":
                userMetaProfileResponse.setPosts(this.getUserPostCount(user));
                userMetaProfileResponse.setPostsOnCommunities(this.getPostsOnCommunity(user, null));
                userMetaProfileResponse.setPostsOnProfile(this.getPostsOnProfile(userMetaProfileResponse));
                break;
            case "totalQuestions":
                userMetaProfileResponse.setTotalQuestions(this.getPostPollQuestionCount(user));
                break;
            case "ownsCommunities":
                userMetaProfileResponse.setOwnsCommunities(this.getCommunityCounts(user));
                break;
            case "followsCommunities":
                userMetaProfileResponse.setFollowsCommunities(this.getFollowingCommunityCounts(user));
                break;
        }
        return userMetaProfileResponse;
    }

    public UserMetaProfileResponse getUserProfileDetails(String userSlug) {
        User user = userCommonService.getUserByUserSlug(userSlug);
        UserMetaProfileResponse userMetaProfileResponse = new UserMetaProfileResponse();
        userMetaProfileResponse.setFollowingTo(this.getUserBeingFollowed(user));
        userMetaProfileResponse.setFollowers(this.getUserFollowers(user));
        userMetaProfileResponse.setPostsOnProfile(this.getPostsOnProfile(userMetaProfileResponse));
        userMetaProfileResponse.setPosts(this.getUserPostCount(user));
        userMetaProfileResponse.setPostsOnCommunities(this.getPostsOnCommunity(user, null));
        userMetaProfileResponse.setTotalQuestions(this.getPostPollQuestionCount(user));
        userMetaProfileResponse.setFollowsCommunities(this.getFollowingCommunityCounts(user));
        userMetaProfileResponse.setOwnsCommunities(this.getCommunityCounts(user));
        return userMetaProfileResponse;
    }

    public int getUserPostCount(User user){
        return postActionRepository.countByUserActor(user);
    }

    public int getPostsOnCommunity(User user, Community community){
        return postActionRepository.countByUserActorAndCommunity(user, community);
    }

    public int getUserFollowers(User user){
        return userFollowerRepository.countByUser(user);
    }

    public int getUserBeingFollowed(User user){
        return userFollowerRepository.countByFollowingUser(user);
    }

    public int getPostsOnProfile(UserMetaProfileResponse userMetaProfileResponse){
        return userMetaProfileResponse.getPosts() - userMetaProfileResponse.getPostsOnProfile();
    }

    public int getPostPollQuestionCount(User user){
        return postActionRepository.countAllByUserActorAndPostType(user, PostType.question);
    }

    public int getFollowingCommunityCounts(User user){
        return communityUserRepository.countByUser(user);
    }

    public int getCommunityCounts(User user){
        return communityRepository.countByOwnerUser(user);
    }
}
