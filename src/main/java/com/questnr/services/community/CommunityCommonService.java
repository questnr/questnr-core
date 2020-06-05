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

    private final String DIR="c";

    private final String COMMUNITY_AVATAR_DIR = "avt";

    @Autowired
    CommunityRepository communityRepository;

    public Community getCommunity(long communityId) {
        Community community = communityRepository.findByCommunityId(communityId);
        if (community != null) {
            return community;
        } else {
            LOGGER.error(Community.class.getName() + " Exception Occurred");
            throw new ResourceNotFoundException("Community not found!");
        }
    }

    public Community getCommunity(String communitySlug) {
        Community community = communityRepository.findFirstBySlug(communitySlug);
        if (community != null) {
            return community;
        } else {
            LOGGER.error(Community.class.getName() + " Exception Occurred");
            throw new ResourceNotFoundException("Community not found!");
        }
    }

    public String getS3BucketUserFolder(long communityId) {
        return Paths.get(DIR,this.getCommunity(communityId).getCommunityId().toString()).toString();
    }

    public String joinPathToFile(String fileName, long communityId) {
        return Paths.get(this.getS3BucketUserFolder(communityId) ,fileName).toString();
    }

    public String getAvatarPathToDir(long communityId){
        return Paths.get(this.getS3BucketUserFolder(communityId), COMMUNITY_AVATAR_DIR).toString();

    }

    public String getAvatarPathToFile(long communityId, String fileName){
//        Path p = Paths.get("Brijesh/brij");
//        Iterator<Path> ps = p.iterator();
//        while(ps.hasNext()){
//            System.out.println(ps.next());
//        }
        return Paths.get(this.getS3BucketUserFolder(communityId), COMMUNITY_AVATAR_DIR, fileName).toString();
    }
}
