package com.questnr.services;

import com.questnr.exceptions.ResourceNotFoundException;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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


    public void readNotification(Long notificationId) {
        User user = userCommonService.getUser();
        Notification notification = notificationRepository.findByUserAndNotificationId(user, notificationId);
        if (notification != null) {
            try {
                notification.setRead(true);
                notificationRepository.save(notification);
            } catch (Exception e) {
                LOGGER.error("Exception occur while save Notification ", e);
                throw new ResourceNotFoundException("Notification not found!");
            }
        } else {
            throw new ResourceNotFoundException("Notification not found!");
        }
    }

    public void deleteNotification(Long notificationId) {
        User user = userCommonService.getUser();
        Notification notification = notificationRepository.findByUserAndNotificationId(user, notificationId);
        if (notification != null) {
            try {
                notificationRepository.delete(notification);
            } catch (Exception e) {
                LOGGER.error("Exception occur while save Notification ", e);
                throw new ResourceNotFoundException("Notification not found!");
            }
        } else {
            throw new ResourceNotFoundException("Notification not found!");
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

    public Integer countUnreadNotifications() {
        User user = userCommonService.getUser();
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("createdAt").descending());
        try {
            List<Notification> notifications = notificationRepository.findAllByUser(user, pageable);
            int count = 0;
            for(Notification notification : notifications){
                if(!notification.isRead()){
                    count++;
                }else{
                    break;
                }
            }
            return count;
        } catch (Exception e) {
            LOGGER.error("Exception occur while fetch Notification by User ", e);
            return 0;
        }
    }
}
