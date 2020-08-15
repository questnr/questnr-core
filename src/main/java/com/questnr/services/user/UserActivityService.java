package com.questnr.services.user;

import com.questnr.model.entities.User;
import com.questnr.model.entities.activity.CommunityActivity;
import com.questnr.model.entities.activity.PostActivity;
import com.questnr.model.entities.activity.UserActivity;
import com.questnr.model.repositories.activity.CommunityActivityRepository;
import com.questnr.model.repositories.activity.PostActivityRepository;
import com.questnr.model.repositories.activity.UserActivityRepository;
import com.questnr.requests.UserActivityRequest;
import com.questnr.responses.UserActivityResponse;
import com.questnr.services.PostActionService;
import com.questnr.services.community.CommunityCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserActivityService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    PostActionService postActionService;

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    PostActivityRepository postActivityRepository;

    @Autowired
    CommunityActivityRepository communityActivityRepository;

    @Autowired
    UserActivityRepository userActivityRepository;

    User getUser() {
        try {
            return userCommonService.getUser();
        } catch (Exception e) {
            return null;
        }
    }

    public UserActivityResponse postSinglePage(UserActivityRequest userActivityRequest) {
        User user = this.getUser();
        UserActivityResponse userActivityResponse = new UserActivityResponse();
        if (userActivityRequest.isUpdateRequest()
                && userActivityRequest.getTrackingId() != null) {
            try {
                PostActivity postActivity = postActivityRepository.findFirstByPostActivityIdAndPostActionAndUserActor(
                        userActivityRequest.getTrackingId(),
                        postActionService.getPostActionById(
                                userActivityRequest.getEntityId()),
                        user
                );
                postActivity.setLastTrack();
                postActivityRepository.save(postActivity);
            } catch (Exception e) {
                LOGGER.error(UserActivityService.class.getName()
                        + " postSinglePage UPDATE " + userActivityRequest.getEntityId());
            }
        } else if (!userActivityRequest.isUpdateRequest()) {
            try {
                PostActivity postActivity = new PostActivity();
                postActivity.setPostAction(postActionService.getPostActionById(
                        userActivityRequest.getEntityId()));
                postActivity.setUserActor(user);
                PostActivity savedPostActivity = postActivityRepository.save(postActivity);
                userActivityResponse.setTrackingId(savedPostActivity.getPostActivityId());
            } catch (Exception e) {
                LOGGER.error(UserActivityService.class.getName()
                        + " postSinglePage CREATE " + userActivityRequest.getEntityId());
            }
        }
        return userActivityResponse;
    }

    public UserActivityResponse communityPage(UserActivityRequest userActivityRequest) {
        User user = this.getUser();
        UserActivityResponse userActivityResponse = new UserActivityResponse();
        if (userActivityRequest.isUpdateRequest()
                && userActivityRequest.getTrackingId() != null) {
            try {
                CommunityActivity communityActivity = communityActivityRepository
                        .findFirstByCommunityActivityIdAndCommunityAndUserActor(
                                userActivityRequest.getTrackingId(),
                                communityCommonService.getCommunity(
                                        userActivityRequest.getEntityId()),
                                user
                        );
                communityActivity.setLastTrack();
                communityActivityRepository.save(communityActivity);
            } catch (Exception e) {
                LOGGER.error(UserActivityService.class.getName()
                        + " communityPage UPDATE " + userActivityRequest.getEntityId());
            }
        } else if (!userActivityRequest.isUpdateRequest()) {
            try {
                CommunityActivity communityActivity = new CommunityActivity();
                communityActivity.setCommunity(communityCommonService.getCommunity(
                        userActivityRequest.getEntityId()));
                communityActivity.setUserActor(user);
                CommunityActivity savedCommunityActivity = communityActivityRepository.save(communityActivity);
                userActivityResponse.setTrackingId(savedCommunityActivity.getCommunityActivityId());
            } catch (Exception e) {
                LOGGER.error(UserActivityService.class.getName()
                        + " communityPage CREATE " + userActivityRequest.getEntityId());
            }
        }
        return userActivityResponse;
    }

    public UserActivityResponse userPage(UserActivityRequest userActivityRequest) {
        User user = this.getUser();
        UserActivityResponse userActivityResponse = new UserActivityResponse();
        if (userActivityRequest.isUpdateRequest()
                && userActivityRequest.getTrackingId() != null) {
            try {
                UserActivity userActivity = userActivityRepository
                        .findFirstByUserActivityIdAndUserAndUserActor(
                                userActivityRequest.getTrackingId(),
                                userCommonService.getUser(
                                        userActivityRequest.getEntityId()),
                                user
                        );
                userActivity.setLastTrack();
                userActivityRepository.save(userActivity);
            } catch (Exception e) {
                LOGGER.error(UserActivityService.class.getName()
                        + " userPage UPDATE " + userActivityRequest.getEntityId());
            }
        } else if (!userActivityRequest.isUpdateRequest()) {
            try {
                UserActivity userActivity = new UserActivity();
                userActivity.setUser(userCommonService.getUser(
                        userActivityRequest.getEntityId()));
                userActivity.setUserActor(user);
                UserActivity savedUserActivity = userActivityRepository.save(userActivity);
                userActivityResponse.setTrackingId(savedUserActivity.getUserActivityId());
            } catch (Exception e) {
                LOGGER.error(UserActivityService.class.getName()
                        + " userPage CREATE " + userActivityRequest.getEntityId());
            }
        }
        return userActivityResponse;
    }
}
