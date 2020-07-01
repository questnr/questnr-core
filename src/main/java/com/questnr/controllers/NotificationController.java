package com.questnr.controllers;

import com.questnr.model.dto.NotificationDTO;
import com.questnr.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @RequestMapping(value = "/user/notification", method = RequestMethod.GET)
    List<NotificationDTO> getNotificationsByUser(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending());
        return notificationService.getNotificationsByUser(pageable);
    }

    @RequestMapping(value = "/user/unread-notification", method = RequestMethod.GET)
    Integer countUnreadNotifications() {
        return notificationService.countUnreadNotifications();
    }


    @RequestMapping(value = "/user/notification/{notificationId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    void readNotification(@PathVariable Long notificationId) {
        notificationService.readNotification(notificationId);
    }

    @RequestMapping(value = "/user/notification/{notificationId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
    }
}
