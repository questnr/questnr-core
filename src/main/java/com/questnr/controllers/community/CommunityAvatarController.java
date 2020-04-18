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
@RequestMapping(value = "/api/v1")
public class CommunityAvatarController {

    @Autowired
    CommunityAvatarAccessService communityAvatarAccessService;

    @Autowired
    CommunityAvatarService communityAvatarService;

    @RequestMapping(value = "/user/community/{communityId}/avatar", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String uploadFile(@PathVariable long communityId, @RequestPart(value = "file") MultipartFile file) {
        /*
         * Community Avatar Security Checking
         * */
        if (communityAvatarAccessService.hasAccessToCommunityAvatar(communityId)) {
            return this.communityAvatarService.uploadAvatar(communityId, file);
        }
        throw new AccessException();
    }

    @RequestMapping(value = "/user/community/{communitySlug}/avatar", method = RequestMethod.GET)
    public String getUserAvatar(@PathVariable String communitySlug) {
        return this.communityAvatarService.getAvatar(communitySlug);
    }

    @RequestMapping(value = "/user/community/{communityId}/avatar", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteFileUsingPathToFile(@PathVariable long communityId) {
        /*
         * Community Avatar Security Checking
         * */
        if (communityAvatarAccessService.hasAccessToCommunityAvatar(communityId)) {
            this.communityAvatarService.deleteAvatar(communityId);
        }else{
            throw new AccessException();
        }
    }

    @RequestMapping(value = "/user/community/{communitySlug}/download-avatar", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> getAvatar(@PathVariable String communitySlug) throws IOException {
        byte[] data = this.communityAvatarService.getAvatarInBytes(communitySlug);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .body(resource);
    }
}