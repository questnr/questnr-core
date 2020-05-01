package com.questnr.services;

import com.questnr.exceptions.InvalidRequestException;
import com.questnr.model.entities.User;
import com.questnr.model.entities.UserNotificationSettings;
import com.questnr.model.repositories.UserNotificationSettingsRepository;
import com.questnr.services.user.UserCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationSettingsService {

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    private UserNotificationSettingsRepository userNotificationSettingsRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public UserNotificationSettings getUserNotificationSettings() {
        User user = userCommonService.getUser();
        UserNotificationSettings userNotificationSettings = userNotificationSettingsRepository.findByUser(user);
        if (userNotificationSettings != null) {
            return userNotificationSettings;
        } else {
            userNotificationSettings = new UserNotificationSettings();
            userNotificationSettings.setUser(user);
            userNotificationSettings.setReceivingNotification(false);
            return userNotificationSettingsRepository.save(userNotificationSettings);
        }
    }

    public void changeReceivingNotificationControl(boolean receivingNotification) {
        User user = userCommonService.getUser();
        try {
            UserNotificationSettings userNotificationSettings = userNotificationSettingsRepository.findByUser(user);
            if (userNotificationSettings != null) {
                userNotificationSettings.setReceivingNotification(receivingNotification);
            } else {
                userNotificationSettings = new UserNotificationSettings();
                userNotificationSettings.setUser(user);
                userNotificationSettings.setReceivingNotification(receivingNotification);
            }
            userNotificationSettingsRepository.save(userNotificationSettings);
        } catch (Exception e) {
            throw new InvalidRequestException("Please, try again!");
        }
    }
}
