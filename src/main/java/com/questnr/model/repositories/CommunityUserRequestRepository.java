package com.questnr.model.repositories;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityUserRequest;
import com.questnr.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityUserRequestRepository extends JpaRepository<CommunityUserRequest, Long> {
    boolean existsByCommunityAndUser(Community community, User user);

    CommunityUserRequest findByCommunityAndUser(Community community, User user);

    List<CommunityUserRequest> findByCommunity(Community community);

    Page<CommunityUserRequest> findByCommunity(Community community, Pageable pageable);

    Long countAllByCommunity(Community community);
}