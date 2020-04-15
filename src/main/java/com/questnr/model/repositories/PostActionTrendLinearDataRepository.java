package com.questnr.model.repositories;

import com.questnr.model.entities.PostActionTrendLinearData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostActionTrendLinearDataRepository extends JpaRepository<PostActionTrendLinearData, Long> {
    Page<PostActionTrendLinearData> findAll(Pageable pageable);
}
