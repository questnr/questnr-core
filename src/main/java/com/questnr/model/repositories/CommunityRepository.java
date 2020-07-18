package com.questnr.model.repositories;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommunityRepository extends JpaRepository<Community, Long> {

    Community findByCommunityId(long communityId);

    int countByCommunityName(String communityName);

    Community findByCommunityName(String name);

    Community findFirstBySlug(String slug);

    @Query("Select c from Community c where LOWER(c.communityName) like %:communityString%")
    Page<Community> findByCommunityNameContaining(@Param("communityString") String communityString, Pageable pageable);

    Page<Community> findByOwnerUser(User user, Pageable pageable);

    int countByOwnerUser(User user);
}
