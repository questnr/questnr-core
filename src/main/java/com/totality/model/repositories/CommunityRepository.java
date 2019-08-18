package com.totality.model.repositories;

import com.totality.model.entities.Community;
import com.totality.responses.CommunityResponse;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommunityRepository  extends JpaRepository <Community, Long>{

  CommunityResponse findById(long id);

  CommunityResponse findAllByCommunityName(String name);

}
