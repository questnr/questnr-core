package com.questnr.model.repositories;

import com.questnr.common.CommunityTrendLinearData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommunityTrendLinearDataRepository extends JpaRepository<CommunityTrendLinearData, Long> {
    Page<CommunityTrendLinearData> findAll(Pageable pageable);
}
