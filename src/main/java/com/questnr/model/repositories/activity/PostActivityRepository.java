package com.questnr.model.repositories.activity;

import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.activity.PostActivity;
import com.questnr.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostActivityRepository extends JpaRepository<PostActivity, Long> {
    PostActivity findFirstByPostActivityIdAndPostActionAndUserActor(
            Long postActionId,
            PostAction postAction,
            User userActor);
}
