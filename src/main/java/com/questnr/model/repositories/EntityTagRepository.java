package com.questnr.model.repositories;

import com.questnr.model.entities.EntityTag;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EntityTagRepository extends JpaRepository<EntityTag, Long> {
    boolean existsByTagValue(String tagValue);
}
