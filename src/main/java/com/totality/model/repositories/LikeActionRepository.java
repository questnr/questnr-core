package com.totality.model.repositories;

import com.totality.model.entities.LikeAction;
import com.totality.model.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeActionRepository extends JpaRepository<LikeAction, Integer> {
    List<LikeAction> findByPost(Post post);

    Long countByPost(Post post);
}
