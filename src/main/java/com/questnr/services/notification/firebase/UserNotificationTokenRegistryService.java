package com.questnr.services.notification.firebase;

import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.User;
import com.questnr.model.entities.UserNotificationTokenRegistry;
import com.questnr.model.repositories.UserNotificationControlRepository;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationTokenRegistryService {

    @Autowired
    private UserNotificationControlRepository userNotificationControlRepository;

    @Autowired
    private UserCommonService userCommonService;

    public void registerUserForPushNotification(String token) {
        UserNotificationTokenRegistry userNotificationTokenRegistry = new UserNotificationTokenRegistry();
        userNotificationTokenRegistry.setToken(token);
        if (!userNotificationControlRepository.existsByToken(token))
            userNotificationControlRepository.save(userNotificationTokenRegistry);
        else {
            try {
                User user = userCommonService.getUser();
                UserNotificationTokenRegistry savedUserNotificationTokenRegistry = userNotificationControlRepository.findByToken(token);
                savedUserNotificationTokenRegistry.setUserActor(user);
                userNotificationControlRepository.save(savedUserNotificationTokenRegistry);
            } catch (ResourceNotFoundException ex) {

            }
        }
    }

    public void unRegisterUserForPushNotification(String token) {
        UserNotificationTokenRegistry userNotificationTokenRegistry = userNotificationControlRepository.findByToken(token);
        if(userNotificationTokenRegistry != null) {
            userNotificationControlRepository.delete(userNotificationTokenRegistry);
        }
    }

}