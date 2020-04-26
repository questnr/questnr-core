package com.questnr.controllers.user;

import com.questnr.exceptions.InvalidRequestException;
import com.questnr.model.dto.UserDTO;
import com.questnr.model.mapper.UserMapper;
import com.questnr.model.projections.UserProjection;
import com.questnr.requests.UpdatePasswordRequest;
import com.questnr.responses.UpdatePasswordResponse;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.user.UserCommonService;
import com.questnr.services.user.UserService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    AmazonS3Client amazonS3Client;

    UserController() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    UserDTO getUser(@PathVariable long userId) {
        return userMapper.toOthersDTO(userCommonService.getUser());
    }

    @RequestMapping(value = "/user/profile/{userSlug}", method = RequestMethod.GET)
    UserDTO getUserByUsername(@PathVariable String userSlug) {
        return userMapper.toOthersDTO(userService.getUserByUserSlug(userSlug));
    }

    @RequestMapping(value = "/user/search/users", method = RequestMethod.GET)
    Page<UserDTO> searchUserString(@RequestParam String userString, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserProjection> userPage = userCommonService.searchUserString(userString, pageable);
        return new PageImpl<>(userMapper.toOthersDTOsFromProjections(userPage.getContent()), pageable, userPage.getTotalElements());
    }

    @RequestMapping(value = "/user/delete/{userId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }

    @RequestMapping(value = "/update-password", method = RequestMethod.POST)
    @ResponseBody
    UpdatePasswordResponse createPasswordResetRequest(
            @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {

        boolean validateToken =
                userService.validatePasswordResetToken(updatePasswordRequest.getResetToken());
        if (validateToken) {
            return userService.updatePassword(updatePasswordRequest);
        }
        throw new InvalidRequestException("Invalid request!");
    }
}
