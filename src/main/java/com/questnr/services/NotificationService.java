package com.questnr.services;

import com.questnr.exceptions.InvalidRequestException;
import com.questnr.model.dto.NotificationDTO;
import com.questnr.model.entities.Notification;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.NotificationMapper;
import com.questnr.model.repositories.NotificationRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.services.user.UserCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    CommonService commonService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationMapper notificationMapper;

    @Autowired
    private NotificationRepository notificationRepository;


    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    public void updateUserNotification(Notification notification, User user) {

        notification = save(notification);
        if (notification == null) {
            throw new InvalidRequestException("Invalid notification");
        }
    }

    public Notification save(Notification notification) {
        try {
            return notificationRepository.save(notification);
        } catch (Exception e) {
            LOGGER.error("Exception occur while save Notification ", e);
            return null;
        }
    }


    public Notification findByUser(User user) {
        try {
            return notificationRepository.findByUser(user);
        } catch (Exception e) {
            LOGGER.error("Exception occur while fetch Notification by User ", e);
            return null;
        }
    }

    public List<NotificationDTO> getNotificationsByUser(Pageable pageable) {
        User user = userCommonService.getUser();
        try {
            List<Notification> notifications = notificationRepository.findAllByUser(user, pageable);
            return notificationMapper.toNotificationDTOs(notifications);
        } catch (Exception e) {
            LOGGER.error("Exception occur while fetch Notification by User ", e);
            return null;
        }
    }
//
//    public Notification createNotificationObject(User user, NotificationType notificationType) {
//        return new Notification(user, notificationType);
//    }

    public Notification findByUserAndNotificationId(User user, Integer notificationId) {
        try {
            return notificationRepository.findByUserAndNotificationId(user, notificationId);
        } catch (Exception e) {
            LOGGER.error("Exception occur while fetch Notification by User and Notification Id ", e);
            return null;
        }
    }
}
