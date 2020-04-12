package com.questnr.controllers.user;

import com.questnr.model.dto.UserDTO;
import com.questnr.model.mapper.UserMapper;
import com.questnr.responses.ResetPasswordResponse;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.ses.AmazonAttachment;
import com.questnr.services.ses.AmazonEmail;
import com.questnr.services.ses.SESProcessor;
import com.questnr.services.ses.enums.SESFrom;
import com.questnr.services.user.UserCommonService;
import com.questnr.services.user.UserService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserCommonService userCommonService;

    UserMapper userMapper;

    @Autowired
    AmazonS3Client amazonS3Client;

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

    @RequestMapping(value = "/search/user/{userString}", method = RequestMethod.GET)
    List<UserDTO> searchUserString(@PathVariable String userString){
        return userMapper.toOthersDTOsFromProjections(userCommonService.searchUserString(userString));
    }

    @RequestMapping(value = "/user/delete/{userId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    @ResponseBody
    ResetPasswordResponse createPasswordResetRequest(@Valid @RequestBody String emailId) {

        // ResponseEntity<ResetPasswordResponse> res = null;
        ResetPasswordResponse response = userService.generatePasswordResetToken(emailId);
        return response;
    }

    @RequestMapping(value = "/testing-mail", method = RequestMethod.POST,  consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String sendMail(@RequestPart(value = "file") MultipartFile file) {

        AmazonEmail amazonEmail = new AmazonEmail(
                "lakkadbrijesh@gmail.com",
                SESFrom.BRIJESH,
                "Hey Brijesh",
                "We have an offer for you :)");

        try{
            AmazonAttachment amazonAttachment = new AmazonAttachment();
            amazonAttachment.setContent(file.getBytes());amazonEmail.setFiles();
            amazonAttachment.setName("Profile pic");
            amazonAttachment.setContentType(file.getContentType());
            amazonEmail.setFiles(amazonAttachment);
        }catch (IOException e){

        }
        SESProcessor.getInstance().add(amazonEmail);

        return "Emails Sent!";
    }
}
