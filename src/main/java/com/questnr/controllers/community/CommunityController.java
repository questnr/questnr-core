package com.questnr.controllers.community;

import com.questnr.access.CommunityAvatarAccessService;
import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.CommunityDTO;
import com.questnr.model.dto.CommunityRequestDTO;
import com.questnr.model.dto.UserDTO;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.model.mapper.UserMapper;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.community.CommunityService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

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
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    // Community CRUD Operations
    @RequestMapping(value = "/user/community", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    CommunityDTO createCommunity(@Valid CommunityRequestDTO communityRequestDTO, @Nullable @RequestParam(value = "file") MultipartFile multipartFile) {
        /*
         * Community Creation Security Checking
         * */
        if (communityAvatarAccessService.hasAccessToCommunityCreation()) {
            return communityMapper.toDTO(communityService.createCommunity(communityMapper.toDomain(communityRequestDTO), multipartFile));
        }
        throw new AccessException();
    }

    @RequestMapping(value = "/user/community/{communityId}", method = RequestMethod.GET)
    CommunityDTO getCommunity(@PathVariable long communityId) {
        return communityMapper.toDTO(communityCommonService.getCommunity(communityId));
    }

    @RequestMapping(value = "/community/{communitySlug}", method = RequestMethod.GET)
    CommunityDTO getCommunity(@PathVariable String communitySlug) {
        return communityMapper.toDTO(communityService.setCommunityMetaInformation(communityCommonService.getCommunity(communitySlug)));
    }

    @RequestMapping(value = "/user/community/{communityId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void deleteCommunity(@PathVariable long communityId) {
        /*
         * Community Deletion Security Checking
         * */
        if (communityAvatarAccessService.hasAccessToCommunityDeletion()) {
            communityService.deleteCommunity(communityId);
        } else {
            throw new AccessException();
        }
    }

    // Get users of a single community.
    @RequestMapping(value = "/user/community/{communitySlug}/users", method = RequestMethod.GET)
    Page<UserDTO> getUsersOfCommunity(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @PathVariable String communitySlug) {
//        List<UserDTO> userDTOS = new ArrayList<>();
//        for(User user: communityService.getUsersFromCommunity(communityId)){
//            userDTOS.add(userMapper.toOthersDTO(user));
//        }
        /*
         * Community Users Fetching Security Checking
         * */
        if (communityAvatarAccessService.hasAccessToGetCommunityUsers()) {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> userPage = communityService.getUsersOfCommunity(communitySlug, pageable);
            return new PageImpl<>(userMapper.toOthersDTOs(userPage.getContent()), pageable, userPage.getTotalElements());
        }
        throw new AccessException();
    }

    // Get community list from community name like string.
    @RequestMapping(value = "/user/community/search/{communityString}", method = RequestMethod.GET)
    Page<CommunityDTO> getCommunitiesFromLikeString(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @PathVariable String communityString) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Community> communityPage = communityService.getCommunitiesFromLikeString(communityString, pageable);
        return new PageImpl<>(communityMapper.toDTOs(communityPage.getContent()), pageable, communityPage.getTotalElements());
    }

    // Search user in community user list
    @RequestMapping(value = "/community/{communitySlug}/user/search/{userString}", method = RequestMethod.GET)
    Page<UserDTO> searchUserInCommunityUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @PathVariable String communitySlug, @PathVariable String userString) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = communityService.searchUserInCommunityUsers(communitySlug, userString, pageable);
        return new PageImpl<>(userMapper.toOthersDTOs(userPage.getContent()), pageable, userPage.getTotalElements());
    }
}
