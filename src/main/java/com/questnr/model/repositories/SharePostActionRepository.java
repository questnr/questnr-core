package com.questnr.model.repositories;

import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.SharedPostAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface SharePostActionRepository extends JpaRepository<SharedPostAction, Long> {
    SharedPostAction findBySharedPostActionId(Long sharedPostActionId);

    Long countAllByPostActionAndCreatedAtBetween(PostAction postAction, Date startingDate, Date endingDate);
}
