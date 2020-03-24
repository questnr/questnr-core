package com.questnr.model.repositories;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.projections.PostActionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

import java.util.List;
import java.util.Set;

public interface PostActionRepository extends JpaRepository <PostAction, Long>, JpaSpecificationExecutorWithProjection<PostAction> {

  Set<PostActionProjection> findAllBySlugOrderByCreatedAtDesc(String slug);

  PostAction findByPostActionId(Long postId);

  Page<PostAction> findAllByUserActorOrderByCreatedAtDesc(User user, Pageable pageable);

  @Query(value = "select pa.postActionId, count(la) as totalLikes, count(pv) as totalVisits, count(ca) as totalComments from PostAction pa left outer join LikeAction la on la.postAction = pa left outer join PostVisit pv on pv.postAction = pa left outer join CommentAction ca on ca.postAction = pa group by pa.postActionId, la.likeActionId, pv.postVisitId, ca.commentActionId ORDER BY totalLikes DESC nulls last, totalVisits DESC nulls last, totalComments DESC nulls last")
  List<Object[]> findAllByTrendingPost(Pageable pageable);

  Page<PostAction> findAllByCommunityOrderByCreatedAtDesc(Community community, Pageable pageable);

  PostAction findByPostActionIdAndCommunity(long postActionId, Community community);

  @Query(value = "select pa from PostAction pa join pa.postMediaList pm where pm.postMediaId = :postMediaId")
  PostAction findByPostMediaListContaining(Long postMediaId);
}
