package com.questnr.model.repositories;

import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostVisit;
import com.questnr.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostVisitRepository extends JpaRepository<PostVisit, Long> {
    Long countByPostActionAndUserActor(PostAction postAction, User user);

    Page<PostVisit> findByPostAction(PostAction postAction, Pageable pageable);

    Optional<PostVisit> findByPostActionAndUserActor(PostAction postAction, User user);
}
