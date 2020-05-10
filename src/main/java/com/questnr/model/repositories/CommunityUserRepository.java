package com.questnr.model.repositories;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityUser;
import com.questnr.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface CommunityUserRepository extends JpaRepository<CommunityUser, Long> {
    boolean existsByCommunityAndUser(Community community, User user);

    Page<CommunityUser> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    List<CommunityUser> findAllByCommunityAndCreatedAtBetween(Community community, Date startingDate, Date endingDate);

    CommunityUser findByCommunityAndUser(Community community, User user);

    int countByUser(User user);

    int countByCommunity(Community community);
}
