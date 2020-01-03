package com.questnr.model.repositories;

import com.questnr.model.entities.CommentAction;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.projections.CommentActionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentActionRepository extends JpaRepository<CommentAction, Long> {

    Long countByPostActionAndUserActor(PostAction postAction, User user);

    CommentAction findByCommentActionId(Long commentId);

    Page<CommentActionProjection> findByPostAction(PostAction postAction, Pageable pageable);

    Optional<CommentAction> findByPostActionAndUserActorAndCommentActionId(PostAction postAction, User user, Long commentActionId);

    Boolean existsByCommentActionId(Long commentId);
}
