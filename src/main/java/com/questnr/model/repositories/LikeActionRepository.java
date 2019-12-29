package com.questnr.model.repositories;

import com.questnr.model.entities.LikeAction;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeActionRepository extends JpaRepository<LikeAction, Long> {

    Long countByPostAction(PostAction postAction);

    Page<LikeAction> findByPostAction(PostAction postAction, Pageable pageable);

    Optional<LikeAction> findByLikeActionIdAndPostActionAndUser(Long id, PostAction postAction, User user);
}
