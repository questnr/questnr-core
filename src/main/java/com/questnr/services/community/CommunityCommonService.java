package com.questnr.services.community;

import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.Community;
import com.questnr.model.repositories.CommunityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
public class CommunityCommonService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final String dir="communities";

    @Autowired
    CommunityRepository communityRepository;

    public Community getCommunity(long communityId) {
        Community community = communityRepository.findById(communityId);
        if (community != null) {
            return community;
        } else {
            LOGGER.error(Community.class.getName() + " Exception Occurred");
            throw new ResourceNotFoundException("Community not found!");
        }
    }

    public String getS3BucketUserFolder(long communityId) {
        return Paths.get(dir,this.getCommunity(communityId).getId().toString()).toString();
    }

    public String joinPathToFile(String fileName, long communityId) {
        return Paths.get(this.getS3BucketUserFolder(communityId) ,fileName).toString();
    }
}
