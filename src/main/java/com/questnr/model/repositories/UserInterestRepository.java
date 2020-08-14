package com.questnr.model.repositories;

import com.questnr.model.entities.EntityTag;
import com.questnr.model.entities.User;
import com.questnr.model.entities.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

    boolean existsByEntityTagAndUser(EntityTag entityTag, User user);
}
