package com.questnr.services;

import com.questnr.model.entities.Community;
import com.questnr.model.repositories.CommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    CommunityRepository communityRepository;

    public boolean slugExistence(String slug, String type) {

        boolean flag = false;
        if (type.equalsIgnoreCase("community")) {
            Community community = communityRepository.findByCommunityName(slug);
            if (community != null) {
                flag = true;
            }
        } else if (type.equalsIgnoreCase("post")) {
            Community community = communityRepository.findByCommunityName(slug);
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
