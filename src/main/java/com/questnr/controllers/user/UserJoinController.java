package com.questnr.controllers.user;

import com.questnr.model.dto.CommunityDTO;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.model.mapper.UserMapper;
import com.questnr.services.user.UserJoinService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class UserJoinController {

    @Autowired
    CommunityMapper communityMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserJoinService userJoinService;

    UserJoinController() {
        userMapper = Mappers.getMapper(UserMapper.class);
        communityMapper = Mappers.getMapper(CommunityMapper.class);
    }

    // Decline the invitation sent to the user
    @RequestMapping(value = "/user/community/joined", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    List<CommunityDTO> getCommunityJoinedList() {
        return communityMapper.toDTOs(userJoinService.getCommunityJoinedList());
    }

    // Decline the invitation sent to the user
    @RequestMapping(value = "/user/community/invitations", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    List<CommunityDTO> getCommunityInvitationList() {
        return communityMapper.toDTOs(userJoinService.getCommunityInvitationList());
    }
}
