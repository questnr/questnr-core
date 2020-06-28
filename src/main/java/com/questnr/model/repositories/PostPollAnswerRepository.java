package com.questnr.model.repositories;

import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostPollAnswer;
import com.questnr.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostPollAnswerRepository extends JpaRepository<PostPollAnswer, Long> {
    boolean existsByPostActionAndUserActor(PostAction postAction, User user);
}
