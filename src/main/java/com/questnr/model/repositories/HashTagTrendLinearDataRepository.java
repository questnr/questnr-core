package com.questnr.model.repositories;

import com.questnr.model.entities.HashTagTrendLinearData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HashTagTrendLinearDataRepository extends JpaRepository<HashTagTrendLinearData, Long> {
    Page<HashTagTrendLinearData> findAll(Pageable pageable);
}
