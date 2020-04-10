package com.questnr.controllers.community;

import com.questnr.access.CommunityJoinAccessService;
import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.CommunityDTO;
import com.questnr.model.mapper.CommunityMapper;
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
    void inviteUserToJoinCommunity(@PathVariable long communityId, @RequestBody Long userId) {
        if (communityJoinAccessService.hasAccessToInviteUser(communityId)) {
            communityJoinService.inviteUserToJoinCommunity(communityId, userId);
        } else {
            throw new AccessException();
        }
    }

    // Ask user to join the community using user email id
    @RequestMapping(value = "/user/join/community/{communityId}/invite/email", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void inviteUserToJoinCommunity(@PathVariable long communityId, @RequestBody String userEmail) {
        if (communityJoinAccessService.hasAccessToInviteUser(communityId)) {
            communityJoinService.inviteUserToJoinCommunity(communityId, userEmail);
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
    void acceptInvitationFromCommunity(@PathVariable long communityId, @RequestBody String userEmail) {
        communityJoinService.actionOnInvitationFromCommunity(communityId, userEmail, true);
    }

    // Decline the invitation sent to the user, using user email id
    @RequestMapping(value = "/user/join/community/{communityId}/invitation/email", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void declineInvitationFromCommunity(@PathVariable long communityId, @RequestBody String userEmail) {
        communityJoinService.actionOnInvitationFromCommunity(communityId, userEmail, false);
    }

    // Revoke join operation
    @RequestMapping(value = "/user/join/community/{communityId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void revokeJoinFromUser(@PathVariable long communityId, @RequestBody Long userId) {
        communityJoinService.revokeJoinFromUser(communityId, userId);
    }

}
