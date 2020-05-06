package com.questnr.model.repositories;

import com.questnr.model.entities.User;
import com.questnr.model.entities.UserNotificationTokenRegistry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserNotificationControlRepository extends JpaRepository<UserNotificationTokenRegistry, Long> {
    boolean existsByToken(String token);

    UserNotificationTokenRegistry findByToken(String token);

    List<UserNotificationTokenRegistry> findAllByUserActor(User user);

    @Query(value = "select unc from UserNotificationTokenRegistry unc where unc.userActor.userId=:userId")
    List<UserNotificationTokenRegistry> findAllByUserActorId(@Param("userId") Long userId);

    UserNotificationTokenRegistry findByUserActorAndToken(User user, String token);

    int countByUserActor(User user);

    List<UserNotificationTokenRegistry> findByUserActorOrderByUpdatedAt(User user, Pageable pageable);
}
