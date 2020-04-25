package com.questnr.services.notification.firebase;

import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.User;
import com.questnr.model.entities.UserNotificationControl;
import com.questnr.model.repositories.UserNotificationControlRepository;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationControlService {

    @Autowired
    private UserNotificationControlRepository userNotificationControlRepository;

    @Autowired
    private UserCommonService userCommonService;

    public void registerUserForPushNotification(String token) {
        UserNotificationControl userNotificationControl = new UserNotificationControl();
        userNotificationControl.setToken(token);
        if (!userNotificationControlRepository.existsByToken(token))
            userNotificationControlRepository.save(userNotificationControl);
        else {
            try {
                User user = userCommonService.getUser();
                UserNotificationControl savedUserNotificationControl = userNotificationControlRepository.findByToken(token);
                savedUserNotificationControl.setUserActor(user);
                userNotificationControlRepository.save(savedUserNotificationControl);
            } catch (ResourceNotFoundException ex) {

            }
        }
    }

    public void unRegisterUserForPushNotification(String token) {
        UserNotificationControl userNotificationControl = userNotificationControlRepository.findByToken(token);
        if(userNotificationControl != null) {
            userNotificationControlRepository.delete(userNotificationControl);
        }
    }

}