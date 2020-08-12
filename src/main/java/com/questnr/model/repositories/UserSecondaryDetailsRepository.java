package com.questnr.model.repositories;

import com.questnr.model.entities.User;
import com.questnr.model.entities.UserSecondaryDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSecondaryDetailsRepository extends JpaRepository<UserSecondaryDetails, Long> {
    UserSecondaryDetails findFirstByUser(User user);
}
