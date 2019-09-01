package com.totality.services;

import com.totality.model.entities.Community;
import com.totality.model.entities.User;
import com.totality.model.repositories.CommunityRepository;
import com.totality.model.repositories.UserRepository;
import com.totality.requests.LoginRequest;
import com.totality.requests.UsersRequest;
import com.totality.responses.CommunityResponse;
import com.totality.responses.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseService {

  @Autowired
  CommunityRepository communityRepository;

  public boolean slugExistence(String slug, String type) {

    boolean flag = false;
    if (type.equalsIgnoreCase("community")) {
      CommunityResponse community = communityRepository.findAllByCommunityName(slug);
      if (community != null) {
        flag = true;
      }
    }
    else if (type.equalsIgnoreCase("post")) {
      CommunityResponse community = communityRepository.findAllByCommunityName(slug);
      if (community != null) {
        flag = true;
      }
    }
//    else if (type.equalsIgnoreCase("institute")) {
//      CommunityResponse community = communityRepository.findAllByCommunityName(slug);
//      if (community != null) {
//        flag = true;
//      }
//    }

    return flag;

  }


}
