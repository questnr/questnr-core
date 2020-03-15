package com.questnr.model.repositories;

import com.questnr.common.enums.PublishStatus;
import com.questnr.model.entities.Community;
import com.questnr.responses.CommunityResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommunityRepository  extends JpaRepository <Community, Long>{

  CommunityResponse findById(long id);

  CommunityResponse findAllByCommunityName(String name);

  List<Community> findAllByStatus(PublishStatus status);

  Community findAllBySlug(String slug);

}
