package com.questnr.controllers.community;

import com.questnr.exceptions.AccessException;
import com.questnr.access.CommunityAvatarAccessService;
import com.questnr.services.community.CommunityAvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/v1/community")
public class CommunityAvatarController {

    final String errorMessage = "You don't have access for the particular operation";

    @Autowired
    CommunityAvatarAccessService communityAvatarAccessService;

    @Autowired
    CommunityAvatarService communityAvatarService;

    @RequestMapping(value = "/{communityId}/avatar", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String uploadFile(@PathVariable long communityId, @RequestPart(value = "file") MultipartFile file) {
        /*
         * Community Avatar Security Checking
         * */
        if (communityAvatarAccessService.hasAccessToCommunityAvatar(communityId)) {
            return this.communityAvatarService.uploadAvatar(communityId, file);
        }
        throw new AccessException(errorMessage);
    }

    @RequestMapping(value = "/{communityId}/avatar", method = RequestMethod.GET)
    public String getUserAvatar(@PathVariable long communityId) {
        return this.communityAvatarService.getUserAvatar(communityId);
    }

    @RequestMapping(value = "/{communityId}/avatar", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteFileUsingPathToFile(@PathVariable long communityId) {
        /*
         * Community Avatar Security Checking
         * */
        if (communityAvatarAccessService.hasAccessToCommunityAvatar(communityId)) {
            this.communityAvatarService.deleteAvatar(communityId);
        }else{
            throw new AccessException(errorMessage);
        }
    }

    @RequestMapping(value = "/{communityId}/download-avatar", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> getAvatar(@PathVariable long communityId) throws IOException {
        byte[] data = this.communityAvatarService.getAvatar(communityId);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .body(resource);
    }
}