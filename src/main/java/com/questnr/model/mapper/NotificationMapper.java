package com.questnr.model.mapper;

import com.questnr.common.NotificationTitles;
import com.questnr.common.enums.NotificationType;
import com.questnr.model.dto.NotificationDTO;
import com.questnr.model.dto.UserDTO;
import com.questnr.model.entities.*;
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

    NotificationMapper() {
        userMapper = Mappers.getMapper(UserMapper.class);
        communityMapper = Mappers.getMapper(CommunityMapper.class);
    }

    public NotificationDTO toNotificationDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        NotificationBase notificationBase = notification.getNotificationBase();
        if (notificationBase instanceof LikeAction) {
            LikeAction likeAction = (LikeAction) notificationBase;
            UserDTO userActor = userMapper.toOthersDTO(likeAction.getUserActor());
            notificationDTO.setMessage(NotificationTitles.LIKE_ACTION);
            notificationDTO.setNotificationType(NotificationType.like);
            notificationDTO.setUserActor(userActor);
            notificationDTO.setPostMedia(postMediaMapper.toPostMediaDTO(likeAction.getPostAction().getPostMediaList().get(0)));
        } else if (notificationBase instanceof CommentAction) {
            CommentAction commentAction = (CommentAction) notificationBase;
            notificationDTO.setMessage(NotificationTitles.COMMENT_ACTION);
            notificationDTO.setNotificationType(NotificationType.comment);
            notificationDTO.setUserActor(userMapper.toOthersDTO(commentAction.getUserActor()));
            notificationDTO.setPostMedia(postMediaMapper.toPostMediaDTO(commentAction.getPostAction().getPostMediaList().get(0)));
        } else if (notificationBase instanceof LikeCommentAction) {
            LikeCommentAction likeCommentAction = (LikeCommentAction) notificationBase;
            notificationDTO.setMessage(NotificationTitles.LIKE_COMMENT_ACTION);
            notificationDTO.setNotificationType(NotificationType.likeComment);
            notificationDTO.setUserActor(userMapper.toOthersDTO(likeCommentAction.getUserActor()));
            notificationDTO.setPostMedia(postMediaMapper.toPostMediaDTO(likeCommentAction.getCommentAction().getPostAction().getPostMediaList().get(0)));
        } else if (notificationBase instanceof CommunityInvitedUser) {
            CommunityInvitedUser communityInvitedUser = (CommunityInvitedUser) notificationBase;

            // Need String.format to insert community name
            notificationDTO.setMessage(String.format(NotificationTitles.INVITATION_ACTION, communityInvitedUser.getCommunity().getCommunityName()));
            notificationDTO.setNotificationType(NotificationType.invitation);
            notificationDTO.setUserActor(userMapper.toOthersDTO(communityInvitedUser.getUserActor()));
            notificationDTO.setCommunity(communityMapper.toCommunityForPostAction(communityInvitedUser.getCommunity()));
        } else if (notificationBase instanceof CommunityUser) {
            CommunityUser communityUser = (CommunityUser) notificationBase;
            // Need String.format to insert community name
            notificationDTO.setMessage(String.format(NotificationTitles.FOLLOWED_COMMUNITY, communityUser.getCommunity().getCommunityName()));
            notificationDTO.setNotificationType(NotificationType.followedCommunity);
            notificationDTO.setUserActor(userMapper.toOthersDTO(communityUser.getUser()));
            notificationDTO.setCommunity(communityMapper.toCommunityForPostAction(communityUser.getCommunity()));
        } else if (notificationBase instanceof UserFollower) {
            UserFollower userFollower = (UserFollower) notificationBase;
            // Need String.format to insert community name
            notificationDTO.setMessage(NotificationTitles.FOLLOWED_USER);
            notificationDTO.setNotificationType(NotificationType.followedUser);
            notificationDTO.setUserActor(userMapper.toOthersDTO(userFollower.getFollowingUser()));
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
