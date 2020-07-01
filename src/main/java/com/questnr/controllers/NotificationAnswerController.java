package com.questnr.controllers;

import com.questnr.model.dto.NotificationDTO;
import com.questnr.services.NotificationAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class NotificationAnswerController {

    @Autowired
    NotificationAnswerService notificationAnswerService;

    @RequestMapping(value = "/user/notification/answer", method = RequestMethod.GET)
    List<NotificationDTO> getAnswerNotificationsByUser(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending());
        return notificationAnswerService.getAnswerNotificationsByUser(pageable);
    }

    @RequestMapping(value = "/user/unread-notification/answer", method = RequestMethod.GET)
    Integer countUnreadNotifications() {
        return notificationAnswerService.countUnreadNotifications();
    }
}
