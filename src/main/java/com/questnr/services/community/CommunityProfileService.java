package com.questnr.services.community;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityTrendLinearData;
import com.questnr.model.repositories.CommunityTrendLinearDataRepository;
import com.questnr.model.repositories.CommunityUserRepository;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.responses.CommunityMetaProfileResponse;
import com.questnr.services.user.UserHomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityProfileService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    CommunityUserRepository communityUserRepository;

    @Autowired
    CommunityTrendLinearDataRepository communityTrendLinearDataRepository;

    @Autowired
    UserHomeService userHomeService;

    public CommunityMetaProfileResponse getCommunityProfileDetails(String communitySlug) {
        return this.getCommunityProfileDetails(communityCommonService.getCommunity(communitySlug));
    }

    public CommunityMetaProfileResponse getCommunityProfileDetails(Community community) {
        CommunityMetaProfileResponse communityMetaProfileResponse = new CommunityMetaProfileResponse();
        communityMetaProfileResponse.setFollowers(communityUserRepository.countByCommunity(community));
        communityMetaProfileResponse.setPosts(postActionRepository.countByCommunity(community));
        CommunityTrendLinearData communityTrendLinearData = communityTrendLinearDataRepository.findByCommunity(community);
        if (communityTrendLinearData != null) {
            communityMetaProfileResponse.setInTrend(true);
            communityMetaProfileResponse.setTrendRank(communityTrendLinearData.getTrendRank());
        } else {
            communityMetaProfileResponse.setInTrend(false);
        }
        return communityMetaProfileResponse;
    }
}
