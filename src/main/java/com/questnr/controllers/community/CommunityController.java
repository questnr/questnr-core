package com.questnr.controllers.community;

import com.questnr.model.dto.CommunityDTO;
import com.questnr.model.dto.UserDTO;
import com.questnr.model.entities.Community;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.model.mapper.UserMapper;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class CommunityController {
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
        return communityMapper.toDTO(communityService.createCommunity(requests, multipartFile));
    }

    @RequestMapping(value = "/community/{communityId}", method = RequestMethod.GET)
    CommunityDTO getCommunity(@PathVariable long communityId) {
        return communityMapper.toDTO(communityCommonService.getCommunity(communityId));
    }

    @RequestMapping(value = "/community/{communityId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void deleteCommunity(@PathVariable long communityId) {
        communityService.deleteCommunity(communityId);
    }

    // Get users of a single community.
    @RequestMapping(value = "/community/{communityId}/users", method = RequestMethod.GET)
    List<UserDTO> getUsersFromCommunity(@PathVariable long communityId){
        return userMapper.toOthersDTOs(communityService.getUsersFromCommunity(communityId));
    }

    // Get community list from community name like string.
    @RequestMapping(value = "/search/community/{communityString}", method = RequestMethod.GET)
    List<CommunityDTO> getCommunitiesFromLikeString(@PathVariable String communityString){
        return communityMapper.toDTOs(communityService.getCommunitiesFromLikeString(communityString));
    }
}
