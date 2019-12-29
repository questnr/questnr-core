package com.questnr.model.repositories;

import java.util.Set;

import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostActionRepository extends JpaRepository <PostAction, Long > {

  Set<PostAction> findAllByPostActionSlug(String slug);

  PostAction findByPostActionId(Long postId);

  Page<PostAction> findAllByUser(User user, Pageable pageable);
}
