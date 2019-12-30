package com.questnr.model.repositories;

import com.questnr.model.entities.LikeAction;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.projections.LikeActionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeActionRepository extends JpaRepository<LikeAction, Long> {

    Long countByPostActionAndUser(PostAction postAction, User user);

    Page<LikeActionProjection> findByPostAction(PostAction postAction, Pageable pageable);

    Optional<LikeAction> findByPostActionAndUser(PostAction postAction, User user);
}
