package com.questnr.model.repositories;

import com.questnr.common.enums.PublishStatus;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.User;
import com.questnr.responses.CommunityResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CommunityRepository  extends JpaRepository <Community, Long>{

  Community findById(long id);

  CommunityResponse findAllByCommunityName(String name);

  List<Community> findAllByStatus(PublishStatus status);

  Community findAllBySlug(String slug);

  @Query("Select c from Community c where c.communityName like %:communityString%")
  List<Community> findByCommunityNameContaining(String communityString);
}
