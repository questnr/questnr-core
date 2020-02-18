package com.questnr.model.repositories;

import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostView;
import com.questnr.model.entities.User;
import com.questnr.model.projections.LikeActionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostViewRepository extends JpaRepository<PostView, Long> {

    Long countByPostActionAndUserActor(PostAction postAction, User user);

    Page<LikeActionProjection> findByPostAction(PostAction postAction, Pageable pageable);

    Optional<PostView> findByPostActionAndUserActor(PostAction postAction, User user);
}
