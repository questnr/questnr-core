package com.questnr.model.repositories;

import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostPollAnswer;
import com.questnr.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface PostPollAnswerRepository extends JpaRepository<PostPollAnswer, Long> {
    boolean existsByPostActionAndUserActor(PostAction postAction, User user);

    PostPollAnswer findFirstByPostActionAndUserActor(PostAction postAction, User user);

    Long countAllByPostActionAndCreatedAtBetween(PostAction postAction, Date startingDate, Date endingDate);

    Long countByPostAction(PostAction postAction);
}
