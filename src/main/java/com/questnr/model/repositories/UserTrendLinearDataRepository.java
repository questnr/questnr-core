package com.questnr.model.repositories;

import com.questnr.model.entities.UserTrendLinearData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTrendLinearDataRepository extends JpaRepository<UserTrendLinearData, Long> {
    Page<UserTrendLinearData> findAll(Pageable pageable);
}
