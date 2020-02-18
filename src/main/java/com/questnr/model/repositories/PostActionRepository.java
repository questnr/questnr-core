package com.questnr.model.repositories;

import java.util.List;
import java.util.Set;

import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.LikeAction;
import com.questnr.model.entities.PostView;
import com.questnr.model.entities.CommentAction;
import com.questnr.model.entities.User;
import com.questnr.model.projections.PostActionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import th.co.geniustree.springdata.jpa.repository.JpaSpecificationExecutorWithProjection;

public interface PostActionRepository extends JpaRepository <PostAction, Long>, JpaSpecificationExecutorWithProjection<PostAction> {

  Set<PostActionProjection> findAllBySlug(String slug);

  PostAction findByPostActionId(Long postId);

  Page<PostActionProjection> findAllByUserActor(User user, Pageable pageable);

  @Query(value = "select pa.postActionId, count(la) as totalLikes, count(pv) as totalPostViews, count(ca) as totalComments from PostAction pa left outer join LikeAction la on la.postAction = pa left outer join PostView pv on pv.postAction = pa left outer join CommentAction ca on ca.postAction = pa group by pa.postActionId, la.likeActionId, pv.postViewId, ca.commentActionId ORDER BY totalLikes DESC nulls last, totalPostViews DESC nulls last, totalComments DESC nulls last")
  List<Object[]> findAllByTrendingPost(Pageable pageable);
}
