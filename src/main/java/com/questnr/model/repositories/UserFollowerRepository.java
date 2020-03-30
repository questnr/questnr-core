package com.questnr.model.repositories;

import com.questnr.model.entities.User;
import com.questnr.model.entities.UserFollower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFollowerRepository extends JpaRepository<UserFollower, Long> {
    boolean existsByUserAndFollowingUser(User userBeingFollowed, User user);

    Integer deleteByUserAndFollowingUser(User userBeingFollowed, User user);

    List<UserFollower> findAllByFollowingUser(User followingUser);
}
