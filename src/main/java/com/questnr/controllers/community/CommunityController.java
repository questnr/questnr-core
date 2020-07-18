package com.questnr.controllers.community;

import com.questnr.access.CommunityAccessService;
import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.SharableLinkDTO;
import com.questnr.model.dto.community.CommunityCardDTO;
import com.questnr.model.dto.community.CommunityDTO;
import com.questnr.model.dto.user.UserOtherDTO;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.model.mapper.UserMapper;
import com.questnr.requests.CommunityNameRequest;
import com.questnr.requests.CommunityRequest;
import com.questnr.requests.CommunityUpdateRequest;
import com.questnr.services.SharableLinkService;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.community.CommunityMetaService;
import com.questnr.services.community.CommunityService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class CommunityController {

    final String errorMessage = "You don't have access for the particular operation";

    @Autowired
    CommunityAccessService communityAccessService;

    @Autowired
    CommunityService communityService;

    @Autowired
    CommunityMapper communityMapper;

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SharableLinkService sharableLinkService;

    @Autowired
            private CommunityMetaService communityMetaService;

    CommunityController() {
        communityMapper = Mappers.getMapper(CommunityMapper.class);
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    // Community CRUD Operations
    @RequestMapping(value = "/user/community", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    CommunityDTO createCommunity(CommunityRequest communityRequest) {
        /*
         * Community Creation Security Checking
         * */
        if (communityAccessService.hasAccessToCommunityCreation()) {
            if (communityRequest.getAvatarFile() == null || communityRequest.getAvatarFile().length == 0) {
                return communityMapper.toDTO(communityService.createCommunity(communityMapper.toDomain(communityRequest)));
            } else {
                return communityMapper.toDTO(communityService.createCommunity(communityMapper.toDomain(communityRequest), communityRequest.getAvatarFile()[0]));
            }
        }
        throw new AccessException();
    }

    // Community CRUD Operations
    @RequestMapping(value = "/user/community/{communityId}", method = RequestMethod.PUT)
    CommunityDTO updateCommunity(@PathVariable Long communityId, @RequestBody CommunityUpdateRequest communityUpdateRequest) {
        /*
         * Community Update Security Checking
         * */
        Community community = communityAccessService.hasAccessToCommunityUpdate(communityId);
        if (community != null) {
            return communityMapper.toDTO(communityService.updateCommunity(community, communityUpdateRequest));
        }
        throw new AccessException();
    }

    // Community list created by user
    @RequestMapping(value = "/user/{userId}/community", method = RequestMethod.GET)
    Page<CommunityDTO> getCommunityListByUser(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        User user = communityAccessService.getCommunityListOfUser(userId);
        if (user != null) {
            Page<Community> communityPage = communityService.getCommunityListOfUser(user, pageable);
            return new PageImpl<>(communityMapper.toDTOs(communityPage.getContent()), pageable, communityPage.getTotalElements());
        }
        throw new AccessException();
    }

    @RequestMapping(value = "/user/community/check-exists", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void checkCommunityExists(@RequestBody CommunityNameRequest communityNameRequest) {
        communityService.checkCommunityNameExists(communityNameRequest.getCommunityName());
    }

    @RequestMapping(value = "/user/community/{communityId}", method = RequestMethod.GET)
    CommunityDTO getCommunity(@PathVariable long communityId) {
        return communityMapper.toDTO(communityCommonService.getCommunity(communityId));
    }

    @RequestMapping(value = "/community/{communitySlug}", method = RequestMethod.GET)
    CommunityDTO getCommunity(@PathVariable String communitySlug) {
        return communityMetaService.setCommunityMetaInformation(communityMapper.toDTO(communityCommonService.getCommunity(communitySlug)));
    }

    @RequestMapping(value = "/user/community/{communityId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void deleteCommunity(@PathVariable long communityId) {
        /*
         * Community Deletion Security Checking
         * */
        if (communityAccessService.hasAccessToCommunityDeletion()) {
            communityService.deleteCommunity(communityId);
        } else {
            throw new AccessException();
        }
    }

    // Get users of a single community.
    @RequestMapping(value = "/user/community/{communitySlug}/users", method = RequestMethod.GET)
    Page<UserOtherDTO> getUsersOfCommunity(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @PathVariable String communitySlug) {
        /*
         * Community Users Fetching Security Checking
         * */
        if (communityAccessService.hasAccessToGetCommunityUsers(communitySlug)) {
            Pageable pageable = PageRequest.of(page, size);
            return communityService.getUsersOfCommunity(communitySlug, pageable);
        }
        throw new AccessException();
    }

    // Get community list from community name like string.
    @RequestMapping(value = "/user/search/communities", method = RequestMethod.GET)
    Page<CommunityCardDTO> getCommunitiesFromLikeString(@RequestParam String communityString, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Community> communityPage = communityService.getCommunitiesFromLikeString(communityString, pageable);
        return new PageImpl<>(communityMapper.toCommunityCards(communityPage.getContent()), pageable, communityPage.getTotalElements());
    }

    // Search user in community user list
    @RequestMapping(value = "/user/community/{communitySlug}/search/users", method = RequestMethod.GET)
    Page<UserOtherDTO> searchUserInCommunityUsers(@RequestParam String userString, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @PathVariable String communitySlug) {
        /*
         * Community Users Fetching Security Checking
         * */
        if (communityAccessService.hasAccessToCommunity(communitySlug)) {
            Pageable pageable = PageRequest.of(page, size);
            return communityService.searchUserInCommunityUsers(communitySlug, userString, pageable);
        }
        throw new AccessException();
    }

    // Get sharable link of the community
    @RequestMapping(value = "/user/community/{communityId}/link", method = RequestMethod.GET)
    SharableLinkDTO getCommunitySharableLink(@PathVariable Long communityId) {
        return sharableLinkService.getCommunitySharableLink(communityId);
    }
}
