package com.questnr.controllers.community;

import com.questnr.responses.CommunityMetaProfileResponse;
import com.questnr.services.community.CommunityProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class CommunityProfileController {

    @Autowired
    CommunityProfileService communityProfileService;

    @RequestMapping(value = "/community/meta/{communitySlug}/info", method = RequestMethod.GET)
    CommunityMetaProfileResponse getCommunityProfileDetails(@PathVariable String communitySlug) {
        return this.communityProfileService.getCommunityProfileDetails(communitySlug);
    }
}
