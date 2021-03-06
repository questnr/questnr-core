package com.questnr.model.repositories;

import com.questnr.model.entities.HashTag;
import com.questnr.model.projections.HashTagProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    HashTag findByHashTagValue(String hashTag);

    @Query("select distinct h from HashTag h where LOWER(h.hashTagValue) like :hashTag%")
    Page<HashTagProjection> findByHashTagValueContaining(@Param("hashTag") String hashTag, Pageable pageable);

    @Query(value = "select h.hash_tag_value as hashTagValue, COUNT(postHashTag.hash_tag_id) as hashTagRank " +
            " from qr_post_hash_tags postHashTag " +
            " left join qr_hash_tags h on h.hash_tag_id = postHashTag.hash_tag_id " +
            " group by h.hash_tag_value, postHashTag.hash_tag_id order by hashTagRank desc", nativeQuery = true)
    Page<Object[]> findAllByHashTagForRank(Pageable pageable);
}
