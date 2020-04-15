package com.questnr.model.repositories;

import com.questnr.model.entities.PostActionTrendData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PostActionTrendDataRepository extends JpaRepository<PostActionTrendData, Long> {

    int countByObservedDate(Date observedDate);

    List<PostActionTrendData> findAllByObservedDateBetween(Date startingDate, Date endingDate);

}
