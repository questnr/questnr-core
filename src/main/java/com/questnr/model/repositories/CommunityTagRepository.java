package com.questnr.model.repositories;

import com.questnr.model.entities.CommunityTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface CommunityTagRepository extends JpaRepository<CommunityTag, Long>,
        JpaSpecificationExecutor<CommunityTag> {
}
