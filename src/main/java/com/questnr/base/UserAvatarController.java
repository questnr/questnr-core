package com.questnr.base;

import com.questnr.services.UserAvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/v1")
public class UserAvatarController {

    @Autowired
    UserAvatarService userAvatarService;

    @RequestMapping(value = "/user/upload-avatar", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return this.userAvatarService.uploadAvatar(file);
    }

    @RequestMapping(value = "/user/delete-avatar", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteFileUsingPathToFile() {
        return this.userAvatarService.deleteAvatar();
    }

    @RequestMapping(value = "/user/download-avatar", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> getAvatar() throws IOException {
        byte[] data = this.userAvatarService.getAvatar();
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .body(resource);

    }
}