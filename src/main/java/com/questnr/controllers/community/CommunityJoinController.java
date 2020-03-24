package com.questnr.controllers.community;

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

    CommunityJoinController() {

        communityMapper = Mappers.getMapper(CommunityMapper.class);
    }

    // Join user to the community
    @RequestMapping(value = "/join/community/{communityId}", method = RequestMethod.POST)
    CommunityDTO joinCommunity(@PathVariable long communityId) {
        return communityMapper.toDTO(communityJoinService.joinCommunity(communityId));
    }

    // Ask user to join the community
    @RequestMapping(value = "/join/community/{communityId}/invite-user", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void inviteUserToJoinCommunity(@PathVariable long communityId, @RequestBody Long userId) {
        communityJoinService.inviteUserToJoinCommunity(communityId, userId);
    }

    // Ask user to join the community using user email id
    @RequestMapping(value = "/join/community/{communityId}/invite-user/email", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void inviteUserToJoinCommunity(@PathVariable long communityId, @RequestBody String userEmail) {
        System.out.println("userEmail");
        System.out.println(userEmail);
        communityJoinService.inviteUserToJoinCommunity(communityId, userEmail);
    }

    // Accept the invitation sent to the user
    @RequestMapping(value = "/join/community/{communityId}/accept-invitation", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    void acceptInvitationFromCommunity(@PathVariable long communityId) {
        communityJoinService.actionOnInvitationFromCommunity(communityId, true);
    }

    // Decline the invitation sent to the user
    @RequestMapping(value = "/join/community/{communityId}/decline-invitation", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    void declineInvitationFromCommunity(@PathVariable long communityId) {
        communityJoinService.actionOnInvitationFromCommunity(communityId, false);
    }

    // Accept the invitation sent to the user, using user email id
    @RequestMapping(value = "/join/community/{communityId}/accept-invitation/user", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void acceptInvitationFromCommunity(@PathVariable long communityId, @RequestBody String userEmail) {
        communityJoinService.actionOnInvitationFromCommunity(communityId, userEmail, true);
    }

    // Decline the invitation sent to the user, using user email id
    @RequestMapping(value = "/join/community/{communityId}/decline-invitation/user", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void declineInvitationFromCommunity(@PathVariable long communityId, @RequestBody String userEmail) {
        communityJoinService.actionOnInvitationFromCommunity(communityId, userEmail, false);
    }


    // Revoke join operation
    @RequestMapping(value = "/join/community/{communityId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void revokeJoinFromUser(@PathVariable long communityId, @RequestBody Long userId) {
        communityJoinService.revokeJoinFromUser(communityId, userId);
    }

}
