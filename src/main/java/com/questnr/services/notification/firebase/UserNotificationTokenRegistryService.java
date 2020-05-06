package com.questnr.services.notification.firebase;

import com.questnr.common.StartingEndingDate;
import com.questnr.model.entities.User;
import com.questnr.model.entities.UserNotificationTokenRegistry;
import com.questnr.model.repositories.UserNotificationControlRepository;
import com.questnr.requests.PushNotificationTokenRegistryRequest;
import com.questnr.requests.RefreshPushNotificationTokenRegistryRequest;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserNotificationTokenRegistryService {

    @Autowired
    private UserNotificationControlRepository userNotificationControlRepository;

    @Autowired
    private UserCommonService userCommonService;

    private final int MAX_NUM_OF_TOKEN_USER_CAN_HAVE = 10;

    public void registerTokenUserForPushNotification(PushNotificationTokenRegistryRequest pushNotificationTokenRegistryRequest) {
        User user = userCommonService.getUser();
        if (userNotificationControlRepository.existsByToken(pushNotificationTokenRegistryRequest.getToken())) {
            UserNotificationTokenRegistry userNotificationTokenRegistry = userNotificationControlRepository.findByToken(pushNotificationTokenRegistryRequest.getToken());

            // last time used
            userNotificationTokenRegistry.updateMetadata();
            if (!userNotificationTokenRegistry.getUserActor().equals(user)) {
                userNotificationTokenRegistry.setUserActor(user);
            }
            userNotificationControlRepository.save(userNotificationTokenRegistry);
        } else {
            if (userNotificationControlRepository.countByUserActor(user) > MAX_NUM_OF_TOKEN_USER_CAN_HAVE) {
                this.deleteOldTokens(user);
            }
            UserNotificationTokenRegistry newUserNotificationTokenRegistry = new UserNotificationTokenRegistry();
            newUserNotificationTokenRegistry.addMetadata();
            newUserNotificationTokenRegistry.setToken(pushNotificationTokenRegistryRequest.getToken());
            newUserNotificationTokenRegistry.setUserActor(user);
            userNotificationControlRepository.saveAndFlush(newUserNotificationTokenRegistry);
        }
    }

    private void deleteOldTokens(User user) {
        StartingEndingDate startingEndingDate = new StartingEndingDate();
        startingEndingDate.setDaysBefore(30);
        List<UserNotificationTokenRegistry> userNotificationTokenRegistryList = userNotificationControlRepository.findByUserActorOrderByUpdatedAt(user, PageRequest.of(0, 10));
        // Remove at least one token
        this.userNotificationControlRepository.delete(userNotificationTokenRegistryList.get(0));
        userNotificationTokenRegistryList.remove(0);
        userNotificationTokenRegistryList.stream().map(userNotificationTokenRegistry -> {
            if (userNotificationTokenRegistry.getUpdatedAt().getTime() > startingEndingDate.getStartingDate().getTime()) {
                this.userNotificationControlRepository.delete(userNotificationTokenRegistry);
            }
            return null;
        });
    }

    public void refreshTokenUserForPushNotification(RefreshPushNotificationTokenRegistryRequest refreshPushNotificationTokenRegistryRequest) {
        User user = userCommonService.getUser();
        UserNotificationTokenRegistry userNotificationTokenRegistry = userNotificationControlRepository.findByToken(refreshPushNotificationTokenRegistryRequest.getExpiredToken());
        userNotificationTokenRegistry.setToken(refreshPushNotificationTokenRegistryRequest.getToken());
        if (!userNotificationTokenRegistry.getUserActor().equals(user)) {
            userNotificationTokenRegistry.setUserActor(user);
        }
        userNotificationTokenRegistry.updateMetadata();
        userNotificationControlRepository.save(userNotificationTokenRegistry);
    }

    public void unRegisterUserForPushNotification(PushNotificationTokenRegistryRequest pushNotificationTokenRegistryRequest) {
//        User user = userCommonService.getUser();
        UserNotificationTokenRegistry userNotificationTokenRegistry = userNotificationControlRepository.findByToken(pushNotificationTokenRegistryRequest.getToken());
        if (userNotificationTokenRegistry != null) {
            userNotificationControlRepository.delete(userNotificationTokenRegistry);
        }
    }
}