package com.questnr.model.repositories;

import com.questnr.model.entities.CommentAction;
import com.questnr.model.entities.LikeCommentAction;
import com.questnr.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeCommentActionRepository extends JpaRepository<LikeCommentAction, Long> {

    Long countByCommentActionAndUserActor(CommentAction commentAction, User user);

    Page<LikeCommentAction> findByCommentAction(CommentAction commentAction, Pageable pageable);

    Optional<LikeCommentAction> findByCommentActionAndUserActor(CommentAction commentAction, User user);
}
