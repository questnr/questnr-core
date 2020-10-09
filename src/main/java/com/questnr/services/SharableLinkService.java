package com.questnr.services;

import com.questnr.common.enums.PostEditorType;
import com.questnr.common.enums.PostType;
import com.questnr.common.enums.SimplifiedPostType;
import com.questnr.model.dto.SharableLinkDTO;
import com.questnr.model.entities.PostAction;
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

    final private String POST_BLOG_ACTION_PATH = "blog";

    final private String POST_QUESTION_ACTION_PATH = "question";

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
        return this.getPostActionSharableLink(postActionRepository.findByPostActionId(postActionId));
    }

    public SharableLinkDTO getPostActionSharableLink(PostAction postAction) {
        SimplifiedPostType simplifiedPostType = SimplifiedPostType.post;
        if (postAction.getPostEditorType() == PostEditorType.blog) {
            simplifiedPostType = SimplifiedPostType.blog;
        } else if (postAction.getPostType() == PostType.question) {
            simplifiedPostType = SimplifiedPostType.question;
        }
        return this.getPostActionSharableLink(postAction.getSlug(), simplifiedPostType);
    }

    public SharableLinkDTO getPostActionSharableLink(String postActionSlug, SimplifiedPostType simplifiedPostType) {
        SharableLinkDTO sharableLinkDTO = new SharableLinkDTO();
        String sharableLink = QUEST_NR_DOMAIN + "/" + Paths.get(simplifiedPostType.jsonValue, postActionSlug).toString();
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
