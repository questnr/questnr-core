package com.questnr.model.repositories;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.HashTag;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.projections.PostActionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface PostActionRepository extends JpaRepository<PostAction, Long>, JpaSpecificationExecutorWithProjection<PostAction> {

    Set<PostActionProjection> findAllBySlugOrderByCreatedAtDesc(String slug);

    PostAction findByPostActionId(Long postId);

    PostAction findByPostActionIdAndUserActor(Long postActionId, User userActor);

    @Query(value = " select pa.post_action_id as postActionId, 0 as postType, null as userWhoShared, pa.created_at as createdAt " +
            " from qr_users qrUser " +
            " left outer join qr_post_actions pa on " +
            " pa.user_id=qrUser.id " +
            " where qrUser.id=:userId group by pa.post_action_id" +
            " union " +
            " select spa.post_action_id as postActionId, 1 as postType, pa.user_id as userWhoShared, spa.created_at as createdAt" +
            " from qr_users qrUser " +
            " inner join qr_shared_post_actions spa " +
            " on spa.user_actor_id=qrUser.id " + // The posts this user shared
            " inner join qr_post_actions pa " +
            " on spa.post_action_id=pa.post_action_id " +
            " where qrUser.id=:userId " +
            " order by 4 desc offset :offset limit :limit"
            , nativeQuery = true)
    List<Object[]> getUserPosts(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);

    @Query("select pa.postActionId, " +
            " COUNT(DISTINCT la.likeActionId) as totalLikes, " +
            " COUNT(DISTINCT ca.commentActionId) as totalComments, " +
            " COUNT(DISTINCT pv.postVisitId) as totalVisits " +
            " from PostAction pa " +
            " left outer join pa.likeActionSet la " +
            " left outer join pa.commentActionSet ca " +
            " left outer join pa.postVisitSet pv " +
            " where pa.createdAt BETWEEN :startingDate and :endingDate " +
            " group by pa.postActionId " +
            " ORDER BY " +
            " totalLikes DESC nulls last, " +
            " totalComments DESC nulls last, " +
            " totalVisits DESC nulls last")
    Page<Object[]> findAllByTrendingPost(@Param("startingDate") Date startingDate, @Param("endingDate") Date endingDate, Pageable pageable);

    Page<PostAction> findAllByCommunityOrderByCreatedAtDesc(Community community, Pageable pageable);

    PostAction findByPostActionIdAndCommunity(long postActionId, Community community);

    @Query(value = "select pa from PostAction pa join pa.postMediaList pm where pm.postMediaId = :postMediaId")
    PostAction findByPostMediaListContaining(Long postMediaId);

    List<PostAction> findAllByUserActor(User user);


    //  @Query(value = "select pa.post_action_id from qr_users u " +
//          " left join qr_user_followers uf on uf.following_user_id = u.id " +
//          " left join qr_post_actions pa on pa.user_id=uf.user_id " +
//          " where u.id=:userId", nativeQuery = true)


    //    @Query("select DISTINCT pa from User user " +
//            " left outer join UserFollower uf on uf.followingUser=user " +
//            " left outer join CommunityUser cu on cu.user=user " +
//            " left outer join Community ca on ca=cu.community " +
//            " left outer join PostAction pa on " +
//            " ((pa.userActor=uf.user and (pa.community is null or pa.community=ca)) " +
//            " or pa.userActor=user or pa.community=ca) " +
//            " where user=:user order by pa.createdAt desc ")


    @Query(value = " select pa.post_action_id as postActionId, 0 as postType, null as userWhoShared, pa.created_at as createdAt " +
            " from qr_users qrUser " +
            " left outer join qr_user_followers uf on uf.following_user_id=qrUser.id " +
            " left outer join qr_community_users cu on cu.user_id=qrUser.id " +
            " left outer join qr_community co on co.community_id=cu.community_id " +
            " left outer join qr_post_actions pa on " +
            " ((pa.user_id=uf.user_id and (pa.community_id is null or pa.community_id=co.community_id)) " +
            " or (pa.community_id=co.community_id) " + // Anyone who posted on the communities followed by the user
            " or pa.user_id=qrUser.id) " +
            " where qrUser.id=:userId group by pa.post_action_id" +
            " union " +
            " select spa.post_action_id as postActionId, 1 as postType, spa.user_actor_id as userWhoShared, spa.created_at as createdAt" +
            " from qr_users qrUser " +
            " inner join qr_user_followers uf on uf.following_user_id=qrUser.id " +
            " inner join qr_shared_post_actions spa " +
            " on spa.user_actor_id=uf.user_id " +
            " where qrUser.id=:userId " +
            " order by 4 desc offset :offset limit :limit"
            , nativeQuery = true)
    List<Object[]> getUserFeed(@Param("userId") Long userId, @Param("offset") int offset, @Param("limit") int limit);

    PostAction findFirstBySlug(String slug);

    List<PostAction> findAllByCommunityAndCreatedAtBetween(Community community, Date startingDate, Date endingDate);

    List<PostAction> findAllByUserActorAndCreatedAtBetween(User user, Date startingDate, Date endingDate);

    Long countAllByHashTagsAndCreatedAtBetween(HashTag hashTag, Date startingDate, Date endingDate);

    Page<PostAction> findByHashTags(HashTag hashTag, Pageable pageable);

    int countByUserActorAndCommunity(User user, Community community);

    int countByUserActor(User user);

    int countByCommunity(Community community);
}
