package com.questnr.model.repositories;

import com.questnr.common.enums.PublishStatus;
import com.questnr.model.entities.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    Community findByCommunityId(long communityId);

    int countByCommunityName(String communityName);

    Community findByCommunityName(String name);

    List<Community> findAllByStatus(PublishStatus status);

    Community findFirstBySlug(String slug);

    @Query("Select c from Community c where c.communityName like %:communityString%")
    Page<Community> findByCommunityNameContaining(String communityString, Pageable pageable);

}
