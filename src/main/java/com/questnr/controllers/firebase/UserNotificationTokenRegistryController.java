package com.questnr.controllers.firebase;

import com.questnr.requests.PushNotificationTokenRegistryRequest;
import com.questnr.requests.RefreshPushNotificationTokenRegistryRequest;
import com.questnr.services.notification.firebase.UserNotificationTokenRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1")
public class UserNotificationTokenRegistryController {

    @Autowired
    private UserNotificationTokenRegistryService userNotificationTokenRegistryService;

    @RequestMapping(value = "/user/push-notification/token", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void registerTokenUserForPushNotification(@Valid @RequestBody PushNotificationTokenRegistryRequest pushNotificationTokenRegistryRequest) {
        userNotificationTokenRegistryService.registerTokenUserForPushNotification(pushNotificationTokenRegistryRequest);
    }

    @RequestMapping(value = "/user/push-notification/refresh-token", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void refreshTokenUserForPushNotification(@Valid @RequestBody RefreshPushNotificationTokenRegistryRequest refreshPushNotificationTokenRegistryRequest) {
        userNotificationTokenRegistryService.refreshTokenUserForPushNotification(refreshPushNotificationTokenRegistryRequest);
    }

    @RequestMapping(value = "/push-notification/token", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void unRegisterUserForPushNotification(@RequestBody PushNotificationTokenRegistryRequest pushNotificationTokenRegistryRequest) {
        userNotificationTokenRegistryService.unRegisterUserForPushNotification(pushNotificationTokenRegistryRequest);
    }
}