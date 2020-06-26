package com.questnr.controllers.community;

import com.questnr.responses.CommunityMetaProfileResponse;
import com.questnr.services.community.CommunityProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class CommunityProfileController {

    @Autowired
    CommunityProfileService communityProfileService;

    @RequestMapping(value = "/community/meta/{communitySlug}/info", method = RequestMethod.GET)
    CommunityMetaProfileResponse getCommunityProfileDetails(@PathVariable String communitySlug) {
        return this.communityProfileService.getCommunityProfileDetails(communitySlug);
    }

    @RequestMapping(value = "/community/meta/{communitySlug}/info/params", method = RequestMethod.GET)
    CommunityMetaProfileResponse getCommunityProfileDetails(@PathVariable String communitySlug,
                                                            @RequestParam(defaultValue = "") String params) {
        return this.communityProfileService.getCommunityProfileDetails(communitySlug, params);
    }
}
