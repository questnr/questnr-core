package com.questnr.controllers.community;

import com.questnr.access.community.CommunityPrivacyAccessService;
import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.community.CommunityDTO;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.requests.CommunityPrivacyUpdateRequest;
import com.questnr.services.community.CommunityPrivacyService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class CommunityPrivacyController {
    @Autowired
    CommunityMapper communityMapper;

    @Autowired
    CommunityPrivacyAccessService communityPrivacyAccessService;

    @Autowired
    CommunityPrivacyService communityPrivacyService;

    CommunityPrivacyController() {
        communityMapper = Mappers.getMapper(CommunityMapper.class);
    }

    // Get the privacy of community
    @RequestMapping(value = "/user/community/{communityId}/privacy", method = RequestMethod.GET)
    CommunityPrivacyUpdateRequest getCommunityPrivacy(@PathVariable Long communityId) {
        return communityPrivacyService.getCommunityPrivacy(communityId);
    }

    // Change the privacy of community
    @RequestMapping(value = "/user/community/{communityId}/privacy", method = RequestMethod.PUT)
    CommunityDTO updateCommunityPrivacy(@PathVariable Long communityId, @RequestBody CommunityPrivacyUpdateRequest communityPrivacyUpdateRequest) {
        if (communityPrivacyAccessService.updateCommunityPrivacy(communityId)) {
            return communityMapper.toDTO(communityPrivacyService.updateCommunityPrivacy(communityId, communityPrivacyUpdateRequest));
        }
        throw new AccessException();
    }
}
