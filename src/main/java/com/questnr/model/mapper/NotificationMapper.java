package com.questnr.model.mapper;

import com.questnr.common.NotificationTitles;
import com.questnr.model.dto.NotificationDTO;
import com.questnr.model.dto.user.UserDTO;
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
    MediaMapper mediaMapper;

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
        notificationDTO.setMetaData(MetaDataMapper.getMetaDataMapper(notification.getCreatedAt(), notification.getUpdatedAt()));
        NotificationBase notificationBase = notification.getNotificationBase();
        if (notificationBase instanceof PostAction) {
            PostAction postAction = (PostAction) notificationBase;
            if(postAction.getCommunity() != null){
                UserDTO userActor = userMapper.toOthersDTO(postAction.getUserActor());
                notificationDTO.setMessage(String.format(postAction.getNotificationTitles(), postAction.getCommunity().getCommunityName()));
                notificationDTO.setNotificationType(postAction.getNotificationType());
                notificationDTO.setUserActor(userActor);
                notificationDTO.setClickAction(sharableLinkService.getPostActionSharableLink(postAction.getSlug()).getClickAction());
                if (this.checkIfPostMediaListIsNotEmpty(postAction))
                    notificationDTO.setPostMedia(mediaMapper.toPostMediaDTO(postAction.getPostMediaList().get(0)));
            }
        } else if (notificationBase instanceof LikeAction) {
            LikeAction likeAction = (LikeAction) notificationBase;
            UserDTO userActor = userMapper.toOthersDTO(likeAction.getUserActor());
            notificationDTO.setMessage(likeAction.getNotificationTitles());
            notificationDTO.setNotificationType(likeAction.getNotificationType());
            notificationDTO.setUserActor(userActor);
            notificationDTO.setClickAction(sharableLinkService.getPostActionSharableLink(likeAction.getPostAction().getSlug()).getClickAction());
            if (this.checkIfPostMediaListIsNotEmpty(likeAction.getPostAction()))
                notificationDTO.setPostMedia(mediaMapper.toPostMediaDTO(likeAction.getPostAction().getPostMediaList().get(0)));
        } else if (notificationBase instanceof CommentAction) {
            CommentAction commentAction = (CommentAction) notificationBase;
            if (commentAction.isChildComment()) {
                notificationDTO.setMessage(NotificationTitles.COMMENT_REPLY_ACTION);
            } else {
                notificationDTO.setMessage(NotificationTitles.COMMENT_ACTION);
            }
            notificationDTO.setNotificationType(commentAction.getNotificationType());
            notificationDTO.setUserActor(userMapper.toOthersDTO(commentAction.getUserActor()));
            notificationDTO.setClickAction(sharableLinkService.getPostActionSharableLink(commentAction.getPostAction().getSlug()).getClickAction());
            if (this.checkIfPostMediaListIsNotEmpty(commentAction.getPostAction()))
                notificationDTO.setPostMedia(mediaMapper.toPostMediaDTO(commentAction.getPostAction().getPostMediaList().get(0)));
        } else if (notificationBase instanceof LikeCommentAction) {
            LikeCommentAction likeCommentAction = (LikeCommentAction) notificationBase;
            notificationDTO.setMessage(likeCommentAction.getNotificationTitles());
            notificationDTO.setNotificationType(likeCommentAction.getNotificationType());
            notificationDTO.setUserActor(userMapper.toOthersDTO(likeCommentAction.getUserActor()));
            notificationDTO.setClickAction(sharableLinkService.getPostActionSharableLink(likeCommentAction.getCommentAction().getPostAction().getSlug()).getClickAction());
            if (this.checkIfPostMediaListIsNotEmpty(likeCommentAction.getCommentAction().getPostAction()))
                notificationDTO.setPostMedia(mediaMapper.toPostMediaDTO(likeCommentAction.getCommentAction().getPostAction().getPostMediaList().get(0)));
        } else if (notificationBase instanceof CommunityInvitedUser) {
            CommunityInvitedUser communityInvitedUser = (CommunityInvitedUser) notificationBase;

            // Need String.format to insert community name
            notificationDTO.setMessage(String.format(communityInvitedUser.getNotificationTitles(), communityInvitedUser.getCommunity().getCommunityName()));
            notificationDTO.setNotificationType(communityInvitedUser.getNotificationType());
            notificationDTO.setUserActor(userMapper.toOthersDTO(communityInvitedUser.getUserActor()));
            notificationDTO.setCommunity(communityMapper.toCommunityListView(communityInvitedUser.getCommunity()));
            notificationDTO.setClickAction(sharableLinkService.getCommunitySharableLink(communityInvitedUser.getCommunity().getSlug()).getClickAction());
        } else if (notificationBase instanceof CommunityUser) {
            CommunityUser communityUser = (CommunityUser) notificationBase;
            // Need String.format to insert community name
            notificationDTO.setMessage(String.format(communityUser.getNotificationTitles(),
                    communityUser.getCommunity().getCommunityName()));
            notificationDTO.setNotificationType(communityUser.getNotificationType());
            notificationDTO.setUserActor(userMapper.toOthersDTO(communityUser.getUser()));
            notificationDTO.setCommunity(communityMapper.toCommunityListView(communityUser.getCommunity()));
            notificationDTO.setClickAction(sharableLinkService.getCommunitySharableLink(communityUser.getCommunity().getSlug()).getClickAction());
        } else if (notificationBase instanceof UserFollower) {
            UserFollower userFollower = (UserFollower) notificationBase;
            // Need String.format to insert community name
            notificationDTO.setMessage(userFollower.getNotificationTitles());
            notificationDTO.setNotificationType(userFollower.getNotificationType());
            notificationDTO.setUserActor(userMapper.toOthersDTO(userFollower.getFollowingUser()));
            notificationDTO.setClickAction(sharableLinkService.getUserSharableLink(userFollower.getFollowingUser().getSlug()).getClickAction());
        } else if (notificationBase instanceof PostPollAnswer) {
            PostPollAnswer postPollAnswer = (PostPollAnswer) notificationBase;
            // Need String.format to insert community name
            notificationDTO.setMessage(postPollAnswer.getNotificationTitles());
            notificationDTO.setNotificationType(postPollAnswer.getNotificationType());
            notificationDTO.setUserActor(userMapper.toOthersDTO(postPollAnswer.getUserActor()));
            notificationDTO.setClickAction(sharableLinkService.getPostActionSharableLink(postPollAnswer.getPostAction().getSlug()).getClickAction());
        } else if (notificationBase instanceof CommunityUserRequest) {
            CommunityUserRequest communityUserRequest = (CommunityUserRequest) notificationBase;
            // Need String.format to insert community name
            notificationDTO.setMessage(String.format(communityUserRequest.getNotificationTitles(),
                    communityUserRequest.getCommunity().getCommunityName()));
            notificationDTO.setNotificationType(communityUserRequest.getNotificationType());
            notificationDTO.setUserActor(userMapper.toOthersDTO(communityUserRequest.getUser()));
            notificationDTO.setClickAction(sharableLinkService.getCommunitySharableLink(communityUserRequest.getCommunity().getSlug()).getClickAction());
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
