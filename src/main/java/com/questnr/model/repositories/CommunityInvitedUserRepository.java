package com.questnr.model.repositories;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityInvitedUser;
import com.questnr.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityInvitedUserRepository extends JpaRepository<CommunityInvitedUser, Long> {
    boolean existsByCommunityAndUser(Community community, User user);

    Page<CommunityInvitedUser> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    CommunityInvitedUser findByCommunityAndUser(Community community, User user);
}
