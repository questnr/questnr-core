package com.questnr.controllers;

import com.questnr.model.dto.NotificationDTO;
import com.questnr.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @RequestMapping(value = "/user/notification", method = RequestMethod.GET)
    List<NotificationDTO> getNotificationsByUser() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        return notificationService.getNotificationsByUser(pageable);
    }

    @RequestMapping(value = "/user/unread-notification", method = RequestMethod.GET)
    Integer countUnreadNotifications() {
        return notificationService.countUnreadNotifications();
    }

}
