package com.questnr.model.repositories;

import com.questnr.model.entities.HashTag;
import com.questnr.model.projections.HashTagProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    @Query("Select h from HashTag h where h.hashTagValue like %:hashTag%")
    Set<HashTagProjection> findByHashTagValueContaining(String hashTag);
}
