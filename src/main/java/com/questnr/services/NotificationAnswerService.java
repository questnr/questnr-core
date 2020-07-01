package com.questnr.services;

import com.questnr.common.enums.NotificationFunctionality;
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

@Service
public class NotificationAnswerService {

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

    public List<NotificationDTO> getAnswerNotificationsByUser(Pageable pageable) {
        User user = userCommonService.getUser();
        try {
            List<Notification> notifications = notificationRepository.findAllByUserAndNotificationFunctionality(user,
                    NotificationFunctionality.answer,
                    pageable);
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
            List<Notification> notifications = notificationRepository.findAllByUserAndNotificationFunctionality(user,
                    NotificationFunctionality.answer,
                    pageable);
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
