package com.questnr.model.repositories;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityUser;
import com.questnr.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityUserRepository  extends JpaRepository<CommunityUser, Long> {
    boolean existsByCommunityAndUser(Community community, User user);
}
