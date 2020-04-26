package com.questnr.controllers.community;

import com.questnr.access.CommunityJoinAccessService;
import com.questnr.common.enums.RelationShipType;
import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.CommunityDTO;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.requests.UserEmailRequest;
import com.questnr.requests.UserIdRequest;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.community.CommunityJoinService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class CommunityJoinController {
    @Autowired
    CommunityJoinService communityJoinService;

    @Autowired
    CommunityMapper communityMapper;

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    CommunityJoinAccessService communityJoinAccessService;

    CommunityJoinController() {

        communityMapper = Mappers.getMapper(CommunityMapper.class);
    }

    // Join user to the community
    @RequestMapping(value = "/user/join/community/{communityId}", method = RequestMethod.POST)
    CommunityDTO joinCommunity(@PathVariable long communityId) {
        if (communityJoinAccessService.hasAccessToJoinCommunity(communityId)) {
            return communityMapper.toDTO(communityJoinService.joinCommunity(communityId));
        } else {
            throw new AccessException();
        }
    }

    // Ask user to join the community
    @RequestMapping(value = "/user/join/community/{communityId}/invite", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void inviteUserToJoinCommunity(@PathVariable long communityId, @RequestBody UserIdRequest userIdRequest) {
        if (communityJoinAccessService.hasAccessToInviteUser(communityId)) {
            communityJoinService.inviteUserToJoinCommunity(communityId, userIdRequest.getUserId());
        } else {
            throw new AccessException();
        }
    }

    // Ask user to join the community using user email id
    @RequestMapping(value = "/user/join/community/{communityId}/invite/email", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void inviteUserToJoinCommunity(@PathVariable long communityId, @RequestBody UserEmailRequest userEmailRequest) {
        if (communityJoinAccessService.hasAccessToInviteUser(communityId)) {
            communityJoinService.inviteUserToJoinCommunity(communityId, userEmailRequest.getUserEmail());
        } else {
            throw new AccessException();
        }
    }

    // Accept the invitation sent to the user
    @RequestMapping(value = "/user/join/community/{communityId}/invitation", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    void acceptInvitationFromCommunity(@PathVariable long communityId) {
        communityJoinService.actionOnInvitationFromCommunity(communityId, true);
    }

    // Decline the invitation sent to the user
    @RequestMapping(value = "/user/join/community/{communityId}/invitation", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void declineInvitationFromCommunity(@PathVariable long communityId) {
        communityJoinService.actionOnInvitationFromCommunity(communityId, false);
    }

    // Accept the invitation sent to the user, using user email id
    @RequestMapping(value = "/user/join/community/{communityId}/invitation/email", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void acceptInvitationFromCommunity(@PathVariable long communityId, @RequestBody UserEmailRequest userEmailRequest) {
        communityJoinService.actionOnInvitationFromCommunity(communityId, userEmailRequest.getUserEmail(), true);
    }

    // Decline the invitation sent to the user, using user email id
    @RequestMapping(value = "/user/join/community/{communityId}/invitation/email", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void declineInvitationFromCommunity(@PathVariable long communityId, @RequestBody UserEmailRequest userEmailRequest) {
        communityJoinService.actionOnInvitationFromCommunity(communityId, userEmailRequest.getUserEmail(), false);
    }

    // Revoke join operation
    @RequestMapping(value = "/user/join/community/{communityId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void revokeJoinFromUser(@PathVariable long communityId, @RequestBody UserIdRequest userIdRequest) {
        communityJoinService.revokeJoinFromUser(communityId, userIdRequest.getUserId());
    }

    // Check user follows this community
    @RequestMapping(value = "/follow/check/community/{communityId}", method = RequestMethod.POST)
    RelationShipType getUserRelationShipWithCommunity(@PathVariable long communityId) {
        return communityJoinService.getUserRelationShipWithCommunity(communityId);
    }
}
