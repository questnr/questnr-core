package com.questnr.services.notification;

import com.questnr.model.entities.*;
import com.questnr.model.mapper.NotificationMapper;
import com.questnr.model.repositories.NotificationRepository;
import com.questnr.model.repositories.UserNotificationControlRepository;
import com.questnr.model.repositories.UserNotificationSettingsRepository;
import com.questnr.services.EmailService;
import com.questnr.services.notification.firebase.PushNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NotificationJob {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    PushNotificationService pushNotificationService;

    @Autowired
    NotificationMapper notificationMapper;

    @Autowired
    UserNotificationControlRepository userNotificationControlRepository;

    @Autowired
    UserNotificationSettingsRepository userNotificationSettingsRepository;

    @Autowired
    EmailService emailService;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public void createNotificationJob(Notification notification) {
        this.createNotificationJob(notification, true);
    }

    public void createNotificationJob(Notification notification, boolean toCreate) {
        if (toCreate)
            NotificationProcessor.getInstance(notificationRepository,
                    userNotificationControlRepository,
                    userNotificationSettingsRepository,
                    pushNotificationService,
                    notificationMapper,
                    emailService).add(notification);
        else NotificationRevertProcessor.getInstance(notificationRepository).add(notification);
    }

    public void createNotificationJob(LikeAction likeAction, boolean toCreate) {
        this.createNotificationJob(new Notification(likeAction), toCreate);
    }

    public void createNotificationJob(CommentAction commentAction, boolean toCreate) {
        if (commentAction.isChildComment()) {
            Set<CommentAction> commentActionSet = commentAction.getParentCommentAction().getChildCommentSet();
            List<CommentAction> commentActionList = commentActionSet.stream().filter(commentAction1 ->
                    !commentAction.equals(commentAction1)
            ).collect(Collectors.toList());

            for (CommentAction childCommentAction : commentActionList) {
                this.createNotificationJob(new Notification(childCommentAction), toCreate);
            }
        }else{
            this.createNotificationJob(new Notification(commentAction), toCreate);
        }
    }

    public void createNotificationJob(PostAction postAction, boolean toCreate) {
        Community community = postAction.getCommunity();
        if(community != null){
            List<User> communityUsers = community.getUsers().stream().map(CommunityUser::getUser).collect(Collectors.toList());
            communityUsers.add(community.getOwnerUser());
            for(User user: communityUsers){
                this.createNotificationJob(new Notification(postAction, user), toCreate);
            }
        }
    }

    public void createNotificationJob(LikeCommentAction likeCommentAction, boolean toCreate) {
        this.createNotificationJob(new Notification(likeCommentAction), toCreate);
    }

    public void createNotificationJob(CommunityInvitedUser communityInvitedUser, boolean toCreate) {
        this.createNotificationJob(new Notification(communityInvitedUser), toCreate);
    }

    public void createNotificationJob(CommunityUser communityUser, boolean toCreate) {
        this.createNotificationJob(new Notification(communityUser), toCreate);
    }

    public void createNotificationJob(UserFollower userFollower, boolean toCreate) {
        this.createNotificationJob(new Notification(userFollower), toCreate);
    }

    public void createNotificationJob(PostPollAnswer postPollAnswer, boolean toCreate) {
        this.createNotificationJob(new Notification(postPollAnswer), toCreate);
    }

    public void createNotificationJob(CommunityUserRequest communityUserRequest, boolean toCreate) {
        this.createNotificationJob(new Notification(communityUserRequest), toCreate);
    }

    public void createNotificationJob(PostAction postAction) {
        this.createNotificationJob(postAction, true);
    }

    public void createNotificationJob(LikeAction likeAction) {
        this.createNotificationJob(likeAction, true);
    }

    public void createNotificationJob(CommentAction commentAction) {
        this.createNotificationJob(commentAction, true);
    }

    public void createNotificationJob(LikeCommentAction likeCommentAction) {
        this.createNotificationJob(likeCommentAction, true);
    }

    public void createNotificationJob(CommunityInvitedUser communityInvitedUser) {
        this.createNotificationJob(communityInvitedUser, true);
    }

    public void createNotificationJob(CommunityUser communityUser) {
        this.createNotificationJob(communityUser, true);
    }

    public void createNotificationJob(UserFollower userFollower) {
        this.createNotificationJob(userFollower, true);
    }

    public void createNotificationJob(PostPollAnswer postPollAnswer) {
        this.createNotificationJob(postPollAnswer, true);
    }

    public void createNotificationJob(CommunityUserRequest communityUserRequest) {
        this.createNotificationJob(new Notification(communityUserRequest), true);
    }
}
