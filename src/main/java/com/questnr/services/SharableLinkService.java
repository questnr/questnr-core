package com.questnr.services;

import com.questnr.model.dto.SharableLinkDTO;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
public class SharableLinkService {

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    UserCommonService userCommonService;

    @Value("${questnr.domain}")
    String QUEST_NR_DOMAIN;

    final private String COMMUNITY_PATH = "community";

    final private String POST_ACTION_PATH = "post";

    final private String USER_PATH = "user";


    public SharableLinkDTO getCommunitySharableLink(Long communityId) {
        String communitySlug = communityCommonService.getCommunity(communityId).getSlug();
        return this.getCommunitySharableLink(communitySlug);
    }

    public SharableLinkDTO getCommunitySharableLink(String communitySlug) {
        SharableLinkDTO sharableLinkDTO = new SharableLinkDTO();
        String sharableLink = QUEST_NR_DOMAIN + "/" + Paths.get(COMMUNITY_PATH, communitySlug).toString();
        sharableLinkDTO.setClickAction(sharableLink);
        return sharableLinkDTO;
    }

    public SharableLinkDTO getPostActionSharableLink(Long postActionId) {
        String postActionSlug = postActionRepository.findByPostActionId(postActionId).getSlug();
        return this.getPostActionSharableLink(postActionSlug);
    }

    public SharableLinkDTO getPostActionSharableLink(String postActionSlug) {
        SharableLinkDTO sharableLinkDTO = new SharableLinkDTO();
        String sharableLink = QUEST_NR_DOMAIN + "/" + Paths.get(POST_ACTION_PATH, postActionSlug).toString();
        sharableLinkDTO.setClickAction(sharableLink);
        return sharableLinkDTO;
    }

    public SharableLinkDTO getUserSharableLink(Long userId) {
        String userSlug = userCommonService.getUser(userId).getSlug();
        return this.getUserSharableLink(userSlug);
    }

    public SharableLinkDTO getUserSharableLink(String userSlug) {
        SharableLinkDTO sharableLinkDTO = new SharableLinkDTO();
        String sharableLink = QUEST_NR_DOMAIN + "/" + Paths.get(USER_PATH, userSlug).toString();
        sharableLinkDTO.setClickAction(sharableLink);
        return sharableLinkDTO;
    }
}
