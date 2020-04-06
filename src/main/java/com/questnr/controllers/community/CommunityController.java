package com.questnr.controllers.community;

import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.CommunityDTO;
import com.questnr.model.dto.UserDTO;
import com.questnr.model.entities.Community;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.model.mapper.UserMapper;
import com.questnr.access.CommunityAvatarAccessService;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.community.CommunityService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class CommunityController {

    final String errorMessage = "You don't have access for the particular operation";

    @Autowired
    CommunityAvatarAccessService communityAvatarAccessService;

    @Autowired
    CommunityService communityService;

    @Autowired
    CommunityMapper communityMapper;

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    UserMapper userMapper;

    CommunityController() {
        communityMapper = Mappers.getMapper(CommunityMapper.class);
    }

    // Community CRUD Operations
    @RequestMapping(value = "/community", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    CommunityDTO createCommunity(@Valid Community requests, @Nullable @RequestParam(value = "file") MultipartFile multipartFile ) {
        /*
         * Community Creation Security Checking
         * */
        if(communityAvatarAccessService.hasAccessToCommunityCreation()) {
            return communityMapper.toDTO(communityService.createCommunity(requests, multipartFile));
        }
        throw new AccessException();
    }

    @RequestMapping(value = "/community/{communityId}", method = RequestMethod.GET)
    CommunityDTO getCommunity(@PathVariable long communityId) {
        return communityMapper.toDTO(communityCommonService.getCommunity(communityId));
    }

    @RequestMapping(value = "/community/slug/{communitySlug}", method = RequestMethod.GET)
    CommunityDTO getCommunity(@PathVariable String communitySlug) {
        return communityMapper.toDTO(communityCommonService.getCommunity(communitySlug));
    }

    @RequestMapping(value = "/community/{communityId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void deleteCommunity(@PathVariable long communityId) {
        /*
         * Community Deletion Security Checking
         * */
        if(communityAvatarAccessService.hasAccessToCommunityDeletion()) {
            communityService.deleteCommunity(communityId);
        }else{
            throw new AccessException();
        }
    }

    // Get users of a single community.
    @RequestMapping(value = "/community/{communitySlug}/users", method = RequestMethod.GET)
    List<UserDTO> getUsersOfCommunity(@PathVariable String communitySlug){
//        List<UserDTO> userDTOS = new ArrayList<>();
//        for(User user: communityService.getUsersFromCommunity(communityId)){
//            userDTOS.add(userMapper.toOthersDTO(user));
//        }
        /*
         * Community Users Fetching Security Checking
         * */
        if(communityAvatarAccessService.hasAccessToGetCommunityUsers()) {
            return userMapper.toOthersDTOs(communityService.getUsersOfCommunity(communitySlug));
        }
        throw new AccessException();
    }

    // Get community list from community name like string.
    @RequestMapping(value = "/search/community/{communityString}", method = RequestMethod.GET)
    List<CommunityDTO> getCommunitiesFromLikeString(@PathVariable String communityString){
        return communityMapper.toDTOs(communityService.getCommunitiesFromLikeString(communityString));
    }

    // Search user in community user list
    @RequestMapping(value = "/search/community/{communitySlug}/user/{userString}", method = RequestMethod.GET)
    List<UserDTO> searchUserInCommunityUsers(@PathVariable String communitySlug, @PathVariable String userString){
        return userMapper.toOthersDTOs(communityService.searchUserInCommunityUsers(communitySlug, userString));
    }
}
