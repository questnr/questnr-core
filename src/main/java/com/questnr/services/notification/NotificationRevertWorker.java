package com.questnr.services.notification;

import com.questnr.common.enums.NotificationType;
import com.questnr.model.entities.*;
import com.questnr.model.repositories.NotificationRepository;
import com.questnr.services.CommonService;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationRevertWorker extends Thread {

    private static final Logger LOG = Logger.getLogger(NotificationRevertWorker.class.getName());

    private boolean iCanContinue = true;

    private final Queue<Notification> queue = new LinkedList<>();

    private Notification item;

    private final int MAX_SLEEP_TIME = 3 * 60 * 60 * 1000; //3 hours

    private NotificationRepository notificationRepository;

    private final int id;

    public NotificationRevertWorker(int id, NotificationRepository notificationRepository) {
        super("NotificationRevertWorker-" + id);
        setDaemon(true);
        this.id = id;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void run() {

        LOG.log(Level.INFO, "NotificationRevert worker-{0} is up and running.", id);

        while (iCanContinue) {
            synchronized (queue) {
                // Check for a new item from the queue
                if (queue.isEmpty()) {
                    // Sleep for it, if there is nothing to do
                    LOG.log(Level.INFO, "Waiting for NotificationRevert to remove...{0}", CommonService.getTime());
                    try {
                        queue.wait(MAX_SLEEP_TIME);
                    } catch (InterruptedException e) {
                        LOG.log(Level.INFO, "Interrupted...{0}", CommonService.getTime());
                    }
                }
                // Take new item from the top of the queue
                item = queue.poll();
                // Null if queue is empty
                if (item == null) {
                    continue;
                }
                try {
                    if (item.getNotificationBase() instanceof LikeAction) {
                        LikeAction likeAction = (LikeAction) item.getNotificationBase();
                        System.out.println(NotificationType.like.getJsonValue());
                        notificationRepository.deleteByNotificationBaseAndType(likeAction.getLikeActionId(), NotificationType.like.getJsonValue(), item.getUser().getUserId());
                    } else if (item.getNotificationBase() instanceof CommentAction) {
                        CommentAction commentAction = (CommentAction) item.getNotificationBase();
                        notificationRepository.deleteByNotificationBaseAndType(commentAction.getCommentActionId(), NotificationType.comment.getJsonValue(), item.getUser().getUserId());
                    } else if (item.getNotificationBase() instanceof LikeCommentAction) {
                        LikeCommentAction likeCommentAction = (LikeCommentAction) item.getNotificationBase();
                        notificationRepository.deleteByNotificationBaseAndType(likeCommentAction.getLikeCommentActionId(), NotificationType.likeComment.getJsonValue(), item.getUser().getUserId());
                    } else if (item.getNotificationBase() instanceof CommunityInvitedUser) {
                        CommunityInvitedUser communityInvitedUser = (CommunityInvitedUser) item.getNotificationBase();
                        notificationRepository.deleteByNotificationBaseAndType(communityInvitedUser.getCommunityInvitationId(), NotificationType.invitation.getJsonValue(), item.getUser().getUserId());
                    }  else if (item.getNotificationBase() instanceof CommunityUser) {
                        CommunityUser communityUser = (CommunityUser) item.getNotificationBase();
                        notificationRepository.deleteByNotificationBaseAndType(communityUser.getCommunityUserId(), NotificationType.followedCommunity.getJsonValue(), item.getUser().getUserId());
                    } else if (item.getNotificationBase() instanceof UserFollower) {
                        UserFollower userFollower = (UserFollower) item.getNotificationBase();
                        notificationRepository.deleteByNotificationBaseAndType(userFollower.getUserFollowerId(), NotificationType.followedUser.getJsonValue(), item.getUser().getUserId());
                    }
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "Exception while removing NotificationRevert ...{0}",
                            ex.getMessage());
                }
            }
        }
    }

    public void add(Notification item) {
        synchronized (queue) {
            queue.add(item);
            queue.notify();
            LOG.log(Level.INFO, "New NotificationRevert added into queue for worker-{0}...", id);
        }
    }

    public void stopWorker() {
        LOG.log(Level.INFO, "Stopping NotificationRevert worker-{0}...", id);
        try {
            iCanContinue = false;
            this.interrupt();
            this.join();
        } catch (InterruptedException | NullPointerException e) {
            LOG.log(Level.SEVERE, "Exception while stopping NotificationRevert worker...{0}",
                    e.getMessage());
        }
    }


}