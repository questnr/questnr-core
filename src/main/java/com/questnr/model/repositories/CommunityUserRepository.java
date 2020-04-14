package com.questnr.model.repositories;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityUser;
import com.questnr.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface CommunityUserRepository  extends JpaRepository<CommunityUser, Long> {
    boolean existsByCommunityAndUser(Community community, User user);

    List<CommunityUser> findAllByCommunityAndCreatedAtBetween(Community community, Date startingDate, Date endingDate);
}
