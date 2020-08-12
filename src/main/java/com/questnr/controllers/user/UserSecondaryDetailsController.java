package com.questnr.controllers.user;

import com.questnr.model.dto.UserSecondaryDetailsDTO;
import com.questnr.model.mapper.UserSecondaryDetailsMapper;
import com.questnr.services.user.UserSecondaryDetailsService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/user/")
public class UserSecondaryDetailsController {

    @Autowired
    private final UserSecondaryDetailsMapper userSecondaryDetailsMapper;

    @Autowired
    private UserSecondaryDetailsService userSecondaryDetailsService;

    UserSecondaryDetailsController() {
        userSecondaryDetailsMapper = Mappers.getMapper(UserSecondaryDetailsMapper.class);
    }

    @RequestMapping(value = "meta/details", method = RequestMethod.GET)
    UserSecondaryDetailsDTO getCommunityProfileDetails() {
        return userSecondaryDetailsMapper.toDTO(this.userSecondaryDetailsService.getUserSecondaryDetails());
    }
}
