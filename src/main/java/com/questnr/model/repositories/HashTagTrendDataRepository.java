package com.questnr.model.repositories;

import com.questnr.model.entities.HashTagTrendData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface HashTagTrendDataRepository extends JpaRepository<HashTagTrendData, Long> {

    int countByObservedDate(Date observedDate);

    List<HashTagTrendData> findAllByObservedDateBetween(Date startingDate, Date endingDate);

}
