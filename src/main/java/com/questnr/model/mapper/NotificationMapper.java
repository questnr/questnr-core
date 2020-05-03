package com.questnr.model.mapper;

import com.questnr.common.NotificationTitles;
import com.questnr.common.enums.NotificationType;
import com.questnr.model.dto.NotificationDTO;
import com.questnr.model.dto.UserDTO;
import com.questnr.model.entities.*;
import com.questnr.services.SharableLinkService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationMapper {
    @Autowired
    PostMediaMapper postMediaMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CommunityMapper communityMapper;

    @Autowired
    SharableLinkService sharableLinkService;

    NotificationMapper() {
        userMapper = Mappers.getMapper(UserMapper.class);
        communityMapper = Mappers.getMapper(CommunityMapper.class);
    }

    private boolean checkIfPostMediaListIsNotEmpty(PostAction postAction) {
        return postAction.getPostMediaList() != null && postAction.getPostMediaList().size() > 0;
    }

    public NotificationDTO toNotificationDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setUser(notification.getUser());
        NotificationBase notificationBase = notification.getNotificationBase();
        if (notificationBase instanceof LikeAction) {
            LikeAction likeAction = (LikeAction) notificationBase;
            UserDTO userActor = userMapper.toOthersDTO(likeAction.getUserActor());
            notificationDTO.setMessage(NotificationTitles.LIKE_ACTION);
            notificationDTO.setNotificationType(NotificationType.like);
            notificationDTO.setUserActor(userActor);
            notificationDTO.setClickAction(sharableLinkService.getPostActionSharableLink(likeAction.getPostAction().getSlug()).getClickAction());
            if (this.checkIfPostMediaListIsNotEmpty(likeAction.getPostAction()))
                notificationDTO.setPostMedia(postMediaMapper.toPostMediaDTO(likeAction.getPostAction().getPostMediaList().get(0)));
        } else if (notificationBase instanceof CommentAction) {
            CommentAction commentAction = (CommentAction) notificationBase;
            if (commentAction.isChildComment()) {
                notificationDTO.setMessage(NotificationTitles.COMMENT_REPLY_ACTION);
            } else {
                notificationDTO.setMessage(NotificationTitles.COMMENT_ACTION);
            }
            notificationDTO.setNotificationType(NotificationType.comment);
            notificationDTO.setUserActor(userMapper.toOthersDTO(commentAction.getUserActor()));
            notificationDTO.setClickAction(sharableLinkService.getPostActionSharableLink(commentAction.getPostAction().getSlug()).getClickAction());
            if (this.checkIfPostMediaListIsNotEmpty(commentAction.getPostAction()))
                notificationDTO.setPostMedia(postMediaMapper.toPostMediaDTO(commentAction.getPostAction().getPostMediaList().get(0)));
        } else if (notificationBase instanceof LikeCommentAction) {
            LikeCommentAction likeCommentAction = (LikeCommentAction) notificationBase;
            notificationDTO.setMessage(NotificationTitles.LIKE_COMMENT_ACTION);
            notificationDTO.setNotificationType(NotificationType.likeComment);
            notificationDTO.setUserActor(userMapper.toOthersDTO(likeCommentAction.getUserActor()));
            notificationDTO.setClickAction(sharableLinkService.getPostActionSharableLink(likeCommentAction.getCommentAction().getPostAction().getSlug()).getClickAction());
            if (this.checkIfPostMediaListIsNotEmpty(likeCommentAction.getCommentAction().getPostAction()))
                notificationDTO.setPostMedia(postMediaMapper.toPostMediaDTO(likeCommentAction.getCommentAction().getPostAction().getPostMediaList().get(0)));
        } else if (notificationBase instanceof CommunityInvitedUser) {
            CommunityInvitedUser communityInvitedUser = (CommunityInvitedUser) notificationBase;

            // Need String.format to insert community name
            notificationDTO.setMessage(String.format(NotificationTitles.INVITATION_ACTION, communityInvitedUser.getCommunity().getCommunityName()));
            notificationDTO.setNotificationType(NotificationType.invitation);
            notificationDTO.setUserActor(userMapper.toOthersDTO(communityInvitedUser.getUserActor()));
            notificationDTO.setCommunity(communityMapper.toCommunityCard(communityInvitedUser.getCommunity()));
            notificationDTO.setClickAction(sharableLinkService.getCommunitySharableLink(communityInvitedUser.getCommunity().getSlug()).getClickAction());
        } else if (notificationBase instanceof CommunityUser) {
            CommunityUser communityUser = (CommunityUser) notificationBase;
            // Need String.format to insert community name
            notificationDTO.setMessage(String.format(NotificationTitles.FOLLOWED_COMMUNITY, communityUser.getCommunity().getCommunityName()));
            notificationDTO.setNotificationType(NotificationType.followedCommunity);
            notificationDTO.setUserActor(userMapper.toOthersDTO(communityUser.getUser()));
            notificationDTO.setCommunity(communityMapper.toCommunityCard(communityUser.getCommunity()));
            notificationDTO.setClickAction(sharableLinkService.getCommunitySharableLink(communityUser.getCommunity().getSlug()).getClickAction());
        } else if (notificationBase instanceof UserFollower) {
            UserFollower userFollower = (UserFollower) notificationBase;
            // Need String.format to insert community name
            notificationDTO.setMessage(NotificationTitles.FOLLOWED_USER);
            notificationDTO.setNotificationType(NotificationType.followedUser);
            notificationDTO.setUserActor(userMapper.toOthersDTO(userFollower.getFollowingUser()));
            notificationDTO.setClickAction(sharableLinkService.getUserSharableLink(userFollower.getFollowingUser().getSlug()).getClickAction());
        }
        notificationDTO.setOpened(notification.isRead());
        notificationDTO.setNotificationId(notification.getNotificationId());
        return notificationDTO;
    }

    public List<NotificationDTO> toNotificationDTOs(List<Notification> notificationList) {
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (Notification notification : notificationList) {
            notificationDTOList.add(this.toNotificationDTO(notification));
        }
        return notificationDTOList;
    }
}
