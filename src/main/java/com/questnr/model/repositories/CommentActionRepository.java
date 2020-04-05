package com.questnr.model.repositories;

import com.questnr.model.entities.CommentAction;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentActionRepository extends JpaRepository<CommentAction, Long> {

    Long countByPostActionAndUserActor(PostAction postAction, User user);

    CommentAction findByCommentActionId(Long commentId);

    Page<CommentAction> findAllByPostActionAndChildComment(PostAction postAction, boolean isChildComment, Pageable pageable);

    CommentAction findByPostActionAndUserActorAndCommentActionId(PostAction postAction, User user, Long commentActionId);

    Boolean existsByCommentActionId(Long commentId);
}
