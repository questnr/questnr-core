package com.questnr.model.repositories;

import com.questnr.model.entities.LikeAction;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface LikeActionRepository extends JpaRepository<LikeAction, Long> {

    Long countByPostActionAndUserActor(PostAction postAction, User user);

    Page<LikeAction> findByPostAction(PostAction postAction, Pageable pageable);

    Optional<LikeAction> findByPostActionAndUserActor(PostAction postAction, User user);

    Long countAllByPostActionAndCreatedAtBetween(PostAction postAction, Date startingDate, Date endingDate);
}
