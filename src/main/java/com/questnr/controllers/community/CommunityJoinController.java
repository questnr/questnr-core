package com.questnr.controllers.community;

import com.questnr.access.community.CommunityJoinAccessService;
import com.questnr.common.enums.RelationShipType;
import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.community.CommunityDTO;
import com.questnr.model.dto.user.UserOtherDTO;
import com.questnr.model.entities.CommunityUser;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.requests.UserEmailRequest;
import com.questnr.requests.UserIdRequest;
import com.questnr.responses.CommunityJoinResponse;
import com.questnr.responses.CommunityMetaProfileResponse;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.community.CommunityJoinService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    // Communities joined by the user
    @RequestMapping(value = "/user/{userId}/join/community", method = RequestMethod.GET)
    Page<CommunityDTO> joinCommunity(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        User user = communityJoinAccessService.getJoinedCommunityList(userId);
        if (user != null) {
            Pageable pageable = PageRequest.of(page, size);
            return communityJoinService.getJoinedCommunityList(user, pageable);
        }
        throw new AccessException();
    }

    // Get the list of invitation from communities for user
    @RequestMapping(value = "/user/community/invitation", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    Page<CommunityDTO> getCommunityInvitationList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return communityJoinService.getCommunityInvitationList(pageable);
    }

    // Join user to the community
    @RequestMapping(value = "/user/join/community/{communityId}", method = RequestMethod.POST)
    CommunityJoinResponse joinCommunity(@PathVariable long communityId) {
        if (communityJoinAccessService.hasAccessToJoinCommunity(communityId)) {
            return communityJoinService.joinCommunity(communityId);
        } else {
            throw new AccessException();
        }
    }

    // Get the list of user requests who wants to join the community
    @RequestMapping(value = "/user/community/{communityId}/users/request", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    Page<UserOtherDTO> getCommunityUserRequestList(@PathVariable Long communityId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "4") int size) {
        if (communityJoinAccessService.getCommunityUserRequestList(communityId)) {
            Pageable pageable = PageRequest.of(page, size);
            return communityJoinService.getCommunityUserRequestList(communityId, pageable);
        }
        throw new AccessException();
    }

    // Accept community user request from list of user requests
    @RequestMapping(value = "/user/community/{communityId}/users/{userId}/request", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void acceptCommunityUserRequest(@PathVariable Long communityId,
                                    @PathVariable Long userId) {
        communityJoinService.actionOnCommunityUserRequest(communityId, userId, true);
    }

    // Decline community user request from list of user requests
    @RequestMapping(value = "/user/community/{communityId}/users/{userId}/request", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void declineCommunityUserRequest(@PathVariable Long communityId,
                                     @PathVariable Long userId) {
        communityJoinService.actionOnCommunityUserRequest(communityId, userId, false);
    }

    // Cancel sent community join request
    @RequestMapping(value = "/user/community/{communityId}/users/request", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void cancelUserOwnedRequest(@PathVariable Long communityId) {
        communityJoinService.cancelUserOwnedRequest(communityId);
    }

    // Ask user to join the community
    @RequestMapping(value = "/user/join/community/{communityId}/invite", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void inviteUserToJoinCommunity(@PathVariable long communityId, @RequestBody UserIdRequest userIdRequest) {
        if (communityJoinAccessService.hasAccessToInviteUser(communityId, userIdRequest.getUserId())) {
            communityJoinService.inviteUserToJoinCommunity(communityId, userIdRequest.getUserId());
        } else {
            throw new AccessException();
        }
    }

    // Ask user to join the community using user email id
    @RequestMapping(value = "/user/join/community/{communityId}/invite/email", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void inviteUserToJoinCommunity(@PathVariable long communityId, @RequestBody UserEmailRequest userEmailRequest) {
        if (communityJoinAccessService.hasAccessToInviteUser(communityId, userEmailRequest.getEmail())) {
            communityJoinService.inviteUserToJoinCommunity(communityId, userEmailRequest.getEmail());
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
        communityJoinService.actionOnInvitationFromCommunity(communityId, userEmailRequest.getEmail(), true);
    }

    // Decline the invitation sent to the user, using user email id
    @RequestMapping(value = "/user/join/community/{communityId}/invitation/email", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void declineInvitationFromCommunity(@PathVariable long communityId, @RequestBody UserEmailRequest userEmailRequest) {
        communityJoinService.actionOnInvitationFromCommunity(communityId, userEmailRequest.getEmail(), false);
    }

    // Revoke join operation
    @RequestMapping(value = "/user/join/community/{communityId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    CommunityMetaProfileResponse revokeJoinFromUser(@PathVariable long communityId, @RequestBody UserIdRequest userIdRequest) {
        if (communityJoinAccessService.revokeJoinFromUser(communityId, userIdRequest.getUserId())) {
            return communityJoinService.revokeJoinFromUser(communityId, userIdRequest.getUserId());
        } else {
            throw new AccessException();
        }
    }

    // Check user follows this community
    @RequestMapping(value = "/user/join/check/community/{communityId}", method = RequestMethod.POST)
    RelationShipType getUserRelationShipWithCommunity(@PathVariable long communityId) {
        return communityJoinService.getUserRelationShipWithCommunity(communityId);
    }

    // Get user list to invite
    @RequestMapping(value = "/user/join/community/{communityId}/users", method = RequestMethod.GET)
    Page<UserOtherDTO> getUserListToInvite(@PathVariable long communityId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        CommunityUser communityUser = communityJoinAccessService.getUserListToInvite(communityId);
        if (communityUser != null) {
            Pageable pageable = PageRequest.of(page, size);
            return communityJoinService.getUserListToInvite(communityUser, pageable);
        }
        throw new AccessException();
    }
}
