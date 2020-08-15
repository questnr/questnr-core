package com.questnr.model.repositories.activity;

import com.questnr.model.entities.User;
import com.questnr.model.entities.activity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    UserActivity findFirstByUserActivityIdAndUserAndUserActor(
            Long communityActivityId,
            User user,
            User userActor);
}
