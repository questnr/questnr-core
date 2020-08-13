package com.questnr.model.repositories;

import com.questnr.model.entities.StaticInterest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StaticInterestRepository extends JpaRepository<StaticInterest, Long> {
    @Query("select distinct si from StaticInterest si where LOWER(si.interest) like %:interestString%")
    Page<StaticInterest> findByInterestContaining(@Param("interestString") String interestString, Pageable pageable);

    @Query("select si from StaticInterest si where LOWER(si.interest)=:interestString")
    StaticInterest findFirstByInterest(@Param("interestString") String interestString);
}
