package com.questnr.model.repositories;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityInvitedUser;
import com.questnr.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityInvitedUserRepository extends JpaRepository<CommunityInvitedUser, Long> {
    boolean existsByCommunityAndUser(Community community, User user);

    CommunityInvitedUser findByCommunityAndUser(Community community, User user);
}
