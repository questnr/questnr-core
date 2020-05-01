package com.questnr.controllers;

import com.questnr.model.dto.UserNotificationSettingsDTO;
import com.questnr.model.mapper.UserNotificationSettingsMapper;
import com.questnr.services.UserNotificationSettingsService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class UserNotificationSettingsController {

    @Autowired
    UserNotificationSettingsService userNotificationSettingsService;

    @Autowired
    UserNotificationSettingsMapper userNotificationSettingsMapper;

    UserNotificationSettingsController() {
        userNotificationSettingsMapper = Mappers.getMapper(UserNotificationSettingsMapper.class);
    }

    @RequestMapping(value = "/user/notification/settings", method = RequestMethod.GET)
    UserNotificationSettingsDTO getUserNotificationSettings() {
        return userNotificationSettingsMapper.toDTO(userNotificationSettingsService.getUserNotificationSettings());
    }

    @RequestMapping(value = "/user/notification/settings/receive/toggle", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void turnOnReceivingNotificationControl() {
        userNotificationSettingsService.changeReceivingNotificationControl(true);
    }

    @RequestMapping(value = "/user/notification/settings/receive/toggle", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void turnOffReceivingNotificationControl() {
        userNotificationSettingsService.changeReceivingNotificationControl(false);
    }
}
