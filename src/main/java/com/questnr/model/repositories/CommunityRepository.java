package com.questnr.model.repositories;

import com.questnr.common.enums.PublishStatus;
import com.questnr.model.entities.Community;
import com.questnr.responses.CommunityResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    Community findByCommunityId(long communityId);

    CommunityResponse findAllByCommunityName(String name);

    List<Community> findAllByStatus(PublishStatus status);

    Community findAllBySlug(String slug);

    @Query("Select c from Community c where c.communityName like %:communityString%")
    List<Community> findByCommunityNameContaining(String communityString);
}
