package com.questnr.model.repositories;

import com.questnr.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmailId(String email);

    User findByUsername(String username);

    User findBySlug(String userSlug);

    User findByUserId(long id);

    boolean existsByUsername(String username);

    boolean existsByEmailId(String emailId);

    @Query("select distinct user from User user where LOWER(user.username) like %:userString% or LOWER(user.firstName) like %:userString% or LOWER(user.lastName) like %:userString%")
    Page<User> findByUserContaining(@Param("userString") String userString, Pageable pageable);

    @Query("select case when count(u)> 0 then true else false end from User u where u.username=:username and u.userId<>:userId")
    Boolean existsByOtherUsername(@Param("userId") Long userId,@Param("username") String username);

    //  @Query("select user.userId, " +
//          " COUNT(DISTINCT pa.postActionId) as totalPosts, " +
//          " COUNT(DISTINCT la.likeActionId) as totalLikes, " +
//          " COUNT(DISTINCT ca.commentActionId) as totalComments, " +
//          " COUNT(DISTINCT pv.postVisitId) as totalVisits, " +
//          " COUNT(DISTINCT pv.postVisitId) as totalVisits " +
//          " from User user " +
//          " left join PostAction pa on user.userId=pa.userActor.userId " +
//          " left outer join pa.likeActionSet la " +
//          " left outer join pa.commentActionSet ca " +
//          " left outer join pa.postVisitSet pv " +
//          " group by user.userId, pa.postActionId " +
//          " ORDER BY " +
//          " totalLikes DESC nulls last, " +
//          " totalComments DESC nulls last, " +
//          " totalVisits DESC nulls last")
//
//    @Query("select user.userId, SIZE(userFollowers) as numberOfFollowers from User user left join user.thisBeingFollowedUserSet userFollowers group by user.userId order by numberOfFollowers desc nulls last")
//    Page<Object[]> findAllByUserFollowerContaining(Pageable pageable);
}
