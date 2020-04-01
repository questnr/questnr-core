package com.questnr.model.repositories;

import com.questnr.model.entities.Community;
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
import java.util.Set;

public interface PostActionRepository extends JpaRepository <PostAction, Long>, JpaSpecificationExecutorWithProjection<PostAction> {

  Set<PostActionProjection> findAllBySlugOrderByCreatedAtDesc(String slug);

  PostAction findByPostActionId(Long postId);

  Page<PostAction> findAllByUserActorOrderByCreatedAtDesc(User user, Pageable pageable);

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
}
