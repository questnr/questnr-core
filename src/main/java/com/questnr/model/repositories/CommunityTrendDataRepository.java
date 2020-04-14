package com.questnr.model.repositories;

import com.questnr.model.entities.CommunityTrendData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface CommunityTrendDataRepository extends JpaRepository<CommunityTrendData, Long> {

    int countByObservedDate(Date observedDate);

    List<CommunityTrendData> findAllByObservedDateBetween(Date startingDate, Date endingDate);

}
