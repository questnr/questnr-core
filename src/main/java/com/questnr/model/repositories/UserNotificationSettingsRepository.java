package com.questnr.model.repositories;

import com.questnr.model.entities.User;
import com.questnr.model.entities.UserNotificationSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNotificationSettingsRepository extends JpaRepository<UserNotificationSettings, Long> {

    boolean existsByUser(User user);

    UserNotificationSettings findByUser(User user);

    boolean existsByUserAndReceivingNotification(User user, boolean receivingNotification);
}
