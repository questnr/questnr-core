package com.questnr.model.repositories;

import com.questnr.model.entities.Notification;
import com.questnr.model.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    Notification findByUser(User user);

    List<Notification> findAllByUser(User user, Pageable pageable);

    Notification findByUserAndNotificationId(User user, Integer notificationId);

}