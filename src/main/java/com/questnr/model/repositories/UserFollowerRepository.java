package com.questnr.model.repositories;

import com.questnr.model.entities.User;
import com.questnr.model.entities.UserFollower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface UserFollowerRepository extends JpaRepository<UserFollower, Long> {
    boolean existsByUserAndFollowingUser(User userBeingFollowed, User user);

    Integer deleteByUserAndFollowingUser(User userBeingFollowed, User user);

    Page<UserFollower> findAllByUserOrderByCreatedAtDesc(User followingUser, Pageable pageable);

    Page<UserFollower> findAllByFollowingUserOrderByCreatedAtDesc(User followingUser, Pageable pageable);

    List<UserFollower> findAllByFollowingUser(User followingUser);

    UserFollower findByUserAndFollowingUser(User user, User followingUser);

    int countByUser(User user);

    int countByFollowingUser(User user);

    List<UserFollower> findAllByUserAndCreatedAtBetween(User user, Date startingDate, Date endingDate);

    @Query("select uf.followingUser from UserFollower uf where uf.user=:user")
    Page<User> getAllByUser(@Param("user") User user, Pageable pageable);
}
