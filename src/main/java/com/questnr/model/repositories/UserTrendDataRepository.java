package com.questnr.model.repositories;

import com.questnr.model.entities.UserTrendData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface UserTrendDataRepository extends JpaRepository<UserTrendData, Long> {

    int countByObservedDate(Date observedDate);

    List<UserTrendData> findAllByObservedDateBetween(Date startingDate, Date endingDate);

}
