package com.questnr.services;

import com.questnr.common.enums.PublishStatus;
import com.questnr.exceptions.InvalidInputException;
import com.questnr.model.entities.Community;
import com.questnr.model.repositories.CommunityRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CommonUserService commonUserService;

    @Autowired
    CommunityRepository communityRepository;

    public Boolean createCommunity(Community communityRequest) {
        Long userId = commonUserService.getUserId();
        Boolean response = false;
        if (communityRequest != null) {
            try {
                communityRequest.addMetadata();
                communityRequest.setOwnerId(userId);
                communityRequest.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                communityRequest.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
                communityRepository.saveAndFlush(communityRequest);
                response = true;
            } catch (Exception e) {
                LOGGER.error(Community.class.getName() + " Exception Occurred");
                response = false;
            }
        } else {
            throw new InvalidInputException(Community.class.getName(), null, null);
        }
        return response;
    }

    public List<Community> findAllCommunityNames() {

        List<Community> communities = null;
        try {
            communities = communityRepository.findAllByStatus(PublishStatus.publish);
        } catch (Exception e) {
            LOGGER.error(Community.class.getName() + " Exception Occurred");
            e.printStackTrace();
        }
        return communities;
    }
}
