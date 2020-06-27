package com.questnr.model.repositories;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityInvitedUser;
import com.questnr.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface CommunityInvitedUserRepository extends JpaRepository<CommunityInvitedUser, Long> {
    boolean existsByCommunityAndUser(Community community, User user);

    Page<CommunityInvitedUser> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    CommunityInvitedUser findFirstByCommunityAndUser(Community community, User user);

    int countAllByCommunityAndUserActorAndUserAndCreatedAtBetween(Community community,
                                                                  User userActor,
                                                                  User user,
                                                                  Date startingDate,
                                                                  Date endingDate);
}
