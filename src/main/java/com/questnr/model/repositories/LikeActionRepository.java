package com.questnr.model.repositories;

import com.questnr.model.entities.LikeAction;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface LikeActionRepository extends JpaRepository<LikeAction, Long> {

    Long countByPostActionAndUserActor(PostAction postAction, User user);

    Page<LikeAction> findByPostAction(PostAction postAction, Pageable pageable);

    Optional<LikeAction> findByPostActionAndUserActor(PostAction postAction, User user);

    Long countAllByPostActionAndCreatedAtBetween(PostAction postAction, Date startingDate, Date endingDate);

    @Query(value = "select userActor from LikeAction la left join la.userActor userActor " +
            " where la.postAction.postActionId=:postActionId and userActor.username LIKE %:userString% or " +
            " userActor.firstName LIKE %:userString% or " +
            " userActor.lastName LIKE %:userString%")
    Page<User> findAllByUserActorContains(@Param("postActionId") Long postActionId, @Param("userString") String userString, Pageable pageable);
}
