package com.questnr.services.community;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityTag;
import com.questnr.model.entities.EntityTag;
import com.questnr.services.CommonService;
import com.questnr.services.EntityTagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommunityTagService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityTagService entityTagService;

    public String[] getCommunityTags(String communityTags) {
        return communityTags.split(",");
    }

    public String getCommunityTag(String inputTag) {
        return CommonService.removeHTMLChars(inputTag).trim().toLowerCase();
    }

    public List<String> parseCommunityTags(List<String> tagList) {
        List<String> newTagList = new ArrayList<>();
        for (String tag : tagList) {
            if (tag != null) {
                newTagList.add(this.getCommunityTag(tag));
            }
        }
        return newTagList;
    }

    public List<CommunityTag> parseAndStoreCommunityTags(List<String> tagList, Community community) {
        List<String> newTagList = new ArrayList<>();
        List<CommunityTag> communityTagList = new ArrayList<>();
        for (String tag : tagList) {
            if (tag != null) {
                String newTag = this.getCommunityTag(tag);
                try {
                    EntityTag entityTag = this.entityTagService.saveEntityTag(newTag);

                    assert entityTag != null;

                    CommunityTag communityTag = new CommunityTag();
                    communityTag.setCommunity(community);
                    communityTag.setEntityTag(entityTag);
                    communityTagList.add(communityTag);
                } catch (Exception e) {
                    LOGGER.error(CommunityService.class.getName() + ": Error in saving EntityTag");
                }
                newTagList.add(newTag);
            }
        }
        return communityTagList;
    }
}
