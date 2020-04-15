package com.questnr.controllers.user;

import com.questnr.model.dto.CommunityDTO;
import com.questnr.model.entities.Community;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.services.CustomPageService;
import com.questnr.services.user.UserJoinService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserJoinController {
    @Autowired
    UserJoinService userJoinService;

    @Autowired
    CommunityMapper communityMapper;

    UserJoinController() {
        communityMapper = Mappers.getMapper(CommunityMapper.class);
    }

    // Decline the invitation sent to the user
    @RequestMapping(value = "/community/joined", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    Page<CommunityDTO> getCommunityJoinedList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Community> communityPage = userJoinService.getCommunityJoinedList(pageable);
        return new PageImpl<>(communityMapper.toDTOs(communityPage.getContent()), pageable, communityPage.getTotalElements());
    }

    // Decline the invitation sent to the user
    @RequestMapping(value = "/community/invitations", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    Page<CommunityDTO> getCommunityInvitationList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Community> communityPage = userJoinService.getCommunityInvitationList(pageable);
        return new PageImpl<>(communityMapper.toDTOs(communityPage.getContent()), pageable, communityPage.getTotalElements());
    }
}
