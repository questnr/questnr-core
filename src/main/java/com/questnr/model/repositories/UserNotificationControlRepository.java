package com.questnr.model.repositories;

import com.questnr.model.entities.User;
import com.questnr.model.entities.UserNotificationControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserNotificationControlRepository extends JpaRepository<UserNotificationControl, Long> {
    boolean existsByToken(String token);

    UserNotificationControl findByToken(String token);

    List<UserNotificationControl> findAllByUserActor(User user);

    @Query(value = "select unc from UserNotificationControl unc where unc.userActor.userId=:userId")
    List<UserNotificationControl> findAllByUserActorId(@Param("userId") Long userId);
}
