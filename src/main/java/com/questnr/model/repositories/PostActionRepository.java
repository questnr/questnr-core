package com.questnr.model.repositories;

import java.util.Set;

import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.projections.PostActionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostActionRepository extends JpaRepository <PostAction, Long > {

  Set<PostActionProjection> findAllBySlug(String slug);

  PostAction findByPostActionId(Long postId);

  Page<PostActionProjection> findAllByUserActor(User user, Pageable pageable);
}
