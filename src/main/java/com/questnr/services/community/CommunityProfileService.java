package com.questnr.services.community;

import com.questnr.common.enums.PostType;
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

    public CommunityMetaProfileResponse getCommunityProfileDetails(String communitySlug, String params) {
        return this.getCommunityProfileDetails(communityCommonService.getCommunity(communitySlug), params);
    }

    public CommunityMetaProfileResponse getCommunityProfileDetails(Community community, String params) {
        String[] paramArray = params.split(",");
        CommunityMetaProfileResponse communityMetaProfileResponse = new CommunityMetaProfileResponse();
        for(String param: paramArray){
            this.setCommunityMetaProfileProperty(community, communityMetaProfileResponse, param.trim());
        }
        return communityMetaProfileResponse;
    }

    public CommunityMetaProfileResponse setCommunityMetaProfileProperty(Community community,
                                                                        CommunityMetaProfileResponse communityMetaProfileResponse,
                                                                        String param){
        switch (param) {
            case "followers":
                communityMetaProfileResponse.setFollowers(this.getCommunityMemberCount(community));
                break;
            case "posts":
                communityMetaProfileResponse.setPosts(this.getPostCount(community));
                break;
            case "totalQuestions":
                communityMetaProfileResponse.setTotalQuestions(this.getPostPollQuestionCount(community));
                break;
            case "isInTrend":
            case "trendRank":
                CommunityTrendLinearData communityTrendLinearData = communityTrendLinearDataRepository.findByCommunity(community);
                if (communityTrendLinearData != null) {
                    communityMetaProfileResponse.setInTrend(true);
                    communityMetaProfileResponse.setTrendRank(communityTrendLinearData.getTrendRank());
                } else {
                    communityMetaProfileResponse.setInTrend(false);
                }
                break;
        }
        return communityMetaProfileResponse;
    }

    public CommunityMetaProfileResponse getCommunityProfileDetails(Community community) {
        CommunityMetaProfileResponse communityMetaProfileResponse = new CommunityMetaProfileResponse();
        communityMetaProfileResponse.setFollowers(this.getCommunityMemberCount(community));
        communityMetaProfileResponse.setPosts(this.getPostCount(community));
        communityMetaProfileResponse.setTotalQuestions(this.getPostPollQuestionCount(community));
        CommunityTrendLinearData communityTrendLinearData = communityTrendLinearDataRepository.findByCommunity(community);
        if (communityTrendLinearData != null) {
            communityMetaProfileResponse.setInTrend(true);
            communityMetaProfileResponse.setTrendRank(communityTrendLinearData.getTrendRank());
        } else {
            communityMetaProfileResponse.setInTrend(false);
        }
        return communityMetaProfileResponse;
    }

    public int getCommunityMemberCount(Community community){
        return communityUserRepository.countByCommunity(community);
    }

    public int getPostCount(Community community){
        return postActionRepository.countByCommunity(community);
    }

    public int getPostPollQuestionCount(Community community){
        return postActionRepository.countAllByCommunityAndPostType(community,
                PostType.question);
    }
}
