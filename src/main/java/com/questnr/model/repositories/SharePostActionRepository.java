package com.questnr.model.repositories;

import com.questnr.model.entities.SharedPostAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharePostActionRepository extends JpaRepository<SharedPostAction, Long> {
    SharedPostAction findBySharedPostActionId(Long sharedPostActionId);
}
