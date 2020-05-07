package com.questnr.model.repositories;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityTrendLinearData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommunityTrendLinearDataRepository extends JpaRepository<CommunityTrendLinearData, Long> {
    Page<CommunityTrendLinearData> findAll(Pageable pageable);

    CommunityTrendLinearData findByCommunity(Community community);
}
