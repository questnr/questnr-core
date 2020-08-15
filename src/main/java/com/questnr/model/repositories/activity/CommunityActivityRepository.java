package com.questnr.model.repositories.activity;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.activity.CommunityActivity;
import com.questnr.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityActivityRepository extends JpaRepository<CommunityActivity, Long> {
    CommunityActivity findFirstByCommunityActivityIdAndCommunityAndUserActor(
            Long communityActivityId,
            Community community,
            User userActor);
}
