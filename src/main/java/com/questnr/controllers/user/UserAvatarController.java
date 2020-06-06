package com.questnr.controllers.user;

import com.questnr.model.dto.AvatarDTO;
import com.questnr.services.user.UserAvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserAvatarController {

    @Autowired
    UserAvatarService userAvatarService;

    @RequestMapping(value = "/avatar", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public AvatarDTO uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return this.userAvatarService.uploadAvatar(file);
    }

    @RequestMapping(value = "/avatar", method = RequestMethod.GET)
    public AvatarDTO getUserAvatar() {
        return this.userAvatarService.getUserAvatar();
    }

    @RequestMapping(value = "/{userSlug}/avatar", method = RequestMethod.GET)
    public AvatarDTO getUserAvatar(@PathVariable String userSlug) {
        return this.userAvatarService.getUserAvatar(userSlug);
    }

    @RequestMapping(value = "/avatar", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteFileUsingPathToFile() {
        this.userAvatarService.deleteAvatar();
    }

    @RequestMapping(value = "/download-avatar", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> getAvatar() {
        byte[] data = this.userAvatarService.getAvatar();
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .body(resource);

    }
}