package com.questnr.services.notification;

import com.questnr.model.dto.NotificationDTO;
import com.questnr.model.entities.Notification;
import com.questnr.model.entities.UserNotificationSettings;
import com.questnr.model.entities.UserNotificationTokenRegistry;
import com.questnr.model.entities.notification.PushNotificationRequest;
import com.questnr.model.mapper.NotificationMapper;
import com.questnr.model.repositories.NotificationRepository;
import com.questnr.model.repositories.UserNotificationControlRepository;
import com.questnr.model.repositories.UserNotificationSettingsRepository;
import com.questnr.services.CommonService;
import com.questnr.services.notification.firebase.PushNotificationService;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class NotificationWorker extends Thread {

    private static final Logger LOG = Logger.getLogger(NotificationWorker.class.getName());

    private boolean iCanContinue = true;

    private final Queue<Notification> queue = new LinkedList<>();

    private Notification item;

    private final int MAX_SLEEP_TIME = 3 * 60 * 60 * 1000; //3 hours

    private NotificationRepository notificationRepository;

    private UserNotificationControlRepository userNotificationControlRepository;

    private UserNotificationSettingsRepository userNotificationSettingsRepository;

    private PushNotificationService pushNotificationService;

    private NotificationMapper notificationMapper;

    private final int id;

    public NotificationWorker(int id, NotificationRepository notificationRepository, UserNotificationControlRepository userNotificationControlRepository, UserNotificationSettingsRepository userNotificationSettingsRepository, PushNotificationService pushNotificationService, NotificationMapper notificationMapper) {
        super("NotificationWorker-" + id);
        setDaemon(true);
        this.id = id;
        this.notificationRepository = notificationRepository;
        this.userNotificationControlRepository = userNotificationControlRepository;
        this.userNotificationSettingsRepository = userNotificationSettingsRepository;
        this.pushNotificationService = pushNotificationService;
        this.notificationMapper = notificationMapper;
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
                    NotificationDTO notificationDTO = this.notificationMapper.toNotificationDTO(item);
                    if (!notificationDTO.getUserActor().getUserId().equals(notificationDTO.getUser().getUserId())) {
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
                                pushNotificationRequest.setTokenList(tokenList);
                                Map<String, String> data = new HashMap<>();
                                data.put("isNotification", "true");
                                data.put("type", "normal");
                                this.pushNotificationService.multicastPushNotificationToTokenWithData(data, pushNotificationRequest);
                            }
                        } catch (Exception ex) {
                            LOG.log(Level.SEVERE, "Exception while sending Notification via firebase ...{0}",
                                    ex.getMessage());
                        }
                        try {
                            this.notificationRepository.save(item);
                        } catch (Exception ex) {
                            LOG.log(Level.SEVERE, "Exception while sending Notification ...{0}",
                                    ex.getMessage());
                        }
                    }
                }catch (Exception ex){
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