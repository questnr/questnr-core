package com.questnr.model.repositories;

import com.questnr.model.entities.EntityTag;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EntityTagRepository extends JpaRepository<EntityTag, Long> {
    boolean existsByTagValue(String tagValue);

    EntityTag findByTagValue(String tagValue);

//    Page<EntityTag> findAllByTagValueInAndCommunityNotNull(List tagValueList, Pageable pageable);
}
