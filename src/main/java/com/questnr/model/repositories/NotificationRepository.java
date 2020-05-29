package com.questnr.model.repositories;

import com.questnr.model.entities.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Notification findByUser(User user);

    List<Notification> findAllByUser(User user, Pageable pageable);

    Notification findByUserAndNotificationId(User user, Long notificationId);

    @Query(value = "delete from qr_user_notifications where notification_base_id=:notificationBaseId and notification_type=:notificationType and user_id=:userId", nativeQuery = true)
    void deleteByNotificationBaseAndType(@Param("notificationBaseId") Long notificationBaseId, @Param("notificationType") String notificationType, @Param("userId") Long userId);
}