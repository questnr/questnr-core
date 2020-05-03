package com.questnr.controllers.firebase;

import com.questnr.services.notification.firebase.UserNotificationTokenRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class UserNotificationTokenRegistryController {

    @Autowired
    private UserNotificationTokenRegistryService userNotificationTokenRegistryService;

    @RequestMapping(value = "/push-notification/token", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void registerUserForPushNotification(@RequestBody String token) {
        userNotificationTokenRegistryService.registerUserForPushNotification(token);
    }

    @RequestMapping(value = "/push-notification/token", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void unRegisterUserForPushNotification(@RequestBody String token) {
        userNotificationTokenRegistryService.unRegisterUserForPushNotification(token);
    }
}