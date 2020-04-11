package com.questnr.controllers.user;

import com.questnr.model.dto.UserDTO;
import com.questnr.model.mapper.UserMapper;
import com.questnr.services.user.UserCommonService;
import com.questnr.services.user.UserService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserCommonService userCommonService;

    UserMapper userMapper;

    UserController(){
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    UserDTO getUser(@PathVariable long userId){
        return userMapper.toOthersDTO(userCommonService.getUser());
    }

    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
    UserDTO getUserByUsername(@PathVariable String username){
        return userMapper.toOthersDTO(userService.getUserByUsername(username));
    }

    @RequestMapping(value = "search/user/{userString}", method = RequestMethod.GET)
    List<UserDTO> searchUserString(@PathVariable String userString){
        return userMapper.toOthersDTOsFromProjections(userCommonService.searchUserString(userString));
    }

    @RequestMapping(value = "/delete-user/{userId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }


}
