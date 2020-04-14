package com.questnr.model.repositories;

import com.questnr.common.enums.PublishStatus;
import com.questnr.model.entities.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    Community findByCommunityId(long communityId);

    Community findByCommunityName(String name);

    List<Community> findAllByStatus(PublishStatus status);

    Community findFirstBySlug(String slug);

    @Query("Select c from Community c where c.communityName like %:communityString%")
    List<Community> findByCommunityNameContaining(String communityString);
//
//    @Query("select co.communityId, " +
//            " COUNT(DISTINCT co.users.userId) as totalFollowers, " +
//            " COUNT(DISTINCT co.postActionSet.postActionId) as totalPosts, " +
//            " COUNT(DISTINCT la.likeActionId) as totalLikes, " +
//            " COUNT(DISTINCT ca.commentActionId) as totalComments, " +
//            " COUNT(DISTINCT pv.postVisitId) as totalVisits " +
//            " from Community co " +
//            " left outer join co.postActionSet pa  " +
//            " left outer join pa.likeActionSet la " +
//            " left outer join pa.commentActionSet ca " +
//            " left outer join pa.postVisitSet pv " +
//            " where pa.createdAt = :specificDate " +
//            " group by pa.postActionId " +
//            " ORDER BY " +
//            " totalFollowers DESC nulls last, " +
//            " totalPosts DESC nulls last, " +
//            " totalLikes DESC nulls last, " +
//            " totalComments DESC nulls last, " +
//            " totalVisits DESC nulls last")
//    List<Object[]> findByCommunityByOnDate(@Param("specificDate") Date specificDate);
}
