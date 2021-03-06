package com.questnr.services.notification;

import com.questnr.common.enums.NotificationType;
import com.questnr.model.dto.NotificationDTO;
import com.questnr.model.entities.*;
import com.questnr.model.entities.notification.PushNotificationRequest;
import com.questnr.model.mapper.NotificationMapper;
import com.questnr.model.repositories.NotificationRepository;
import com.questnr.model.repositories.UserNotificationControlRepository;
import com.questnr.model.repositories.UserNotificationSettingsRepository;
import com.questnr.services.CommonService;
import com.questnr.services.EmailService;
import com.questnr.services.notification.firebase.PushNotificationService;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class NotificationWorker extends Thread {

    private static final Logger LOG = Logger.getLogger(NotificationWorker.class.getName());

    private boolean iCanContinue = true;

    private final Queue<Notification> queue = new LinkedList<>();

    private final NotificationRepository notificationRepository;

    private final UserNotificationControlRepository userNotificationControlRepository;

    private final UserNotificationSettingsRepository userNotificationSettingsRepository;

    private final PushNotificationService pushNotificationService;

    private final NotificationMapper notificationMapper;

    private final EmailService emailService;

    private final int id;

    public NotificationWorker(int id,
                              NotificationRepository notificationRepository,
                              UserNotificationControlRepository userNotificationControlRepository,
                              UserNotificationSettingsRepository userNotificationSettingsRepository,
                              PushNotificationService pushNotificationService,
                              NotificationMapper notificationMapper,
                              EmailService emailService) {
        super("NotificationWorker-" + id);
        setDaemon(true);
        this.id = id;
        this.notificationRepository = notificationRepository;
        this.userNotificationControlRepository = userNotificationControlRepository;
        this.userNotificationSettingsRepository = userNotificationSettingsRepository;
        this.pushNotificationService = pushNotificationService;
        this.notificationMapper = notificationMapper;
        this.emailService = emailService;
    }

    @Override
    public void run() {

        LOG.log(Level.INFO, "Notification worker-{0} is up and running.", id);

        while (iCanContinue) {
            synchronized (queue) {
                // Check for a new item from the queue
                if (queue.isEmpty()) {
                    // Sleep for it, if there is nothing to do
                    LOG.log(Level.INFO, "Waiting for Notification to send...{0}", CommonService.getTime());
                    try {
                        // 2 minutes
                        int MAX_SLEEP_TIME = 2 * 60 * 1000;
                        queue.wait(MAX_SLEEP_TIME);
                    } catch (InterruptedException e) {
                        LOG.log(Level.INFO, "Interrupted...{0}", CommonService.getTime());
                    }
                }
                // Take new item from the top of the queue
                Notification item = queue.poll();
                // Null if queue is empty
                if (item == null) {
                    continue;
                }
                try {
                    NotificationDTO notificationDTO = this.notificationMapper.toNotificationDTO(item);
                    if (!notificationDTO.getUserActor().getUserId().equals(notificationDTO.getUser().getUserId()) ||
                            notificationDTO.getNotificationType() == NotificationType.communityAccepted) {
                        try {
                            UserNotificationSettings userNotificationSettings = userNotificationSettingsRepository.findByUser(notificationDTO.getUser());
                            if (userNotificationSettings == null || userNotificationSettings.isReceivingNotification()) {
                                PushNotificationRequest pushNotificationRequest = new PushNotificationRequest();
                                pushNotificationRequest.setTitle(notificationDTO.getUserActor().getUsername());
                                pushNotificationRequest.setMessage(notificationDTO.getMessage());
                                if (notificationDTO.getCommunity() != null && !CommonService.isNull(notificationDTO.getCommunity().getAvatarDTO().getAvatarLink())) {
                                    pushNotificationRequest.setImgURL(notificationDTO.getCommunity().getAvatarDTO().getAvatarLink());
                                } else if (notificationDTO.getPostMedia() != null && !CommonService.isNull(notificationDTO.getPostMedia().getPostMediaLink())) {
                                    pushNotificationRequest.setImgURL(notificationDTO.getPostMedia().getPostMediaLink());
                                }
                                pushNotificationRequest.setClickAction(notificationDTO.getClickAction());
                                List<UserNotificationTokenRegistry> userNotificationTokenRegistryList = this.userNotificationControlRepository.findAllByUserActor(notificationDTO.getUser());
                                List<String> tokenList = userNotificationTokenRegistryList.stream().map(UserNotificationTokenRegistry::getToken).collect(Collectors.toList());
                                if (tokenList.size() > 0) {
                                    pushNotificationRequest.setTokenList(tokenList);
                                    Map<String, String> data = new HashMap<>();
                                    data.put("isNotification", "true");
                                    data.put("type", item.getNotificationFunctionality().getJsonValue());
                                    // If notification belongs to creation of post
                                    if (item.getNotificationBase() instanceof PostAction) {
                                        PostAction postAction = (PostAction) item.getNotificationBase();
                                        // If the post is a community post
                                        if (postAction.getCommunity().getSlug() != null) {
                                            data.put("purposeType", "post_created");
                                            data.put("communitySlug", postAction.getCommunity().getSlug());
                                            data.put("postId", postAction.getPostActionId().toString());
                                        }
                                    }
                                    this.pushNotificationService.multicastPushNotificationToTokenWithData(data, pushNotificationRequest);
                                }
                            }
                        } catch (Exception ex) {
                            LOG.log(Level.SEVERE, "Exception while sending Notification via firebase ...{0}",
                                    ex.getMessage());
                        }
                        try {
                            if (item.getNotificationBase() instanceof CommunityInvitedUser) {
                                this.emailService.sendInvitationEmailToUserToJoinCommunity(notificationDTO);
                            }
                            this.notificationRepository.save(item);
                        } catch (Exception ex) {
                            LOG.log(Level.SEVERE, "Exception while sending Notification ...{0}",
                                    ex.getMessage());
                        }
                    }
                } catch (Exception ex) {
                    LOG.log(Level.SEVERE, "Exception while mapping to NotificationDTO ...{0}",
                            ex.getMessage());
                }
            }
        }
    }

    public void add(Notification item) {
        synchronized (queue) {
            queue.add(item);
            queue.notify();
            LOG.log(Level.INFO, "New Notification added into queue for  worker-{0}...", id);
        }
    }

    public void stopWorker() {
        LOG.log(Level.INFO, "Stopping Notification worker-{0}...", id);
        try {
            iCanContinue = false;
            this.interrupt();
            this.join();
        } catch (InterruptedException | NullPointerException e) {
            LOG.log(Level.SEVERE, "Exception while stopping Notification worker...{0}",
                    e.getMessage());
        }
    }


}