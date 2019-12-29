package com.questnr.model.repositories;

import com.questnr.common.enums.PublishStatus;
import com.questnr.model.entities.Community;
import com.questnr.responses.CommunityResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommunityRepository  extends JpaRepository <Community, Long>{

  CommunityResponse findById(long id);

  CommunityResponse findAllByCommunityName(String name);

//  @Query("select c.id as id, c.communityName as communityName, c.slug as slug from Community c where c.status='publish'")
  List<Community> findAllByStatus(PublishStatus status);

  Community findAllBySlug(String slug);

}
