package com.questnr.services.community;

import com.questnr.model.entities.Community;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.requests.CommunityPrivacyUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityPrivacyService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    CommunityCommonService communityCommonService;

    public CommunityPrivacyUpdateRequest getCommunityPrivacy(Long communityId){
        Community community = communityCommonService.getCommunity(communityId);
        CommunityPrivacyUpdateRequest communityPrivacyUpdateRequest = new CommunityPrivacyUpdateRequest();
        communityPrivacyUpdateRequest.setCommunityPrivacy(community.getCommunityPrivacy());
        return communityPrivacyUpdateRequest;
    }

    public Community updateCommunityPrivacy(Long communityId, CommunityPrivacyUpdateRequest communityPrivacyUpdateRequest) {
        Community community = communityCommonService.getCommunity(communityId);
        community.setCommunityPrivacy(communityPrivacyUpdateRequest.getCommunityPrivacy());
        return communityRepository.save(community);
    }
}
