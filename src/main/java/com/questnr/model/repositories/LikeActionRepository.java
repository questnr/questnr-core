package com.questnr.model.repositories;

import com.questnr.model.entities.LikeAction;
import com.questnr.model.entities.PostAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface LikeActionRepository extends JpaRepository<LikeAction, Long> {
    Set<LikeAction> findByPost(PostAction post);

    Long countByPostAction(PostAction postAction);

    Page<LikeAction> findByPostActionId(Long postId, Pageable pageable);

    Optional<LikeAction> findByIdAndPostId(Long id, Long postId);
}
