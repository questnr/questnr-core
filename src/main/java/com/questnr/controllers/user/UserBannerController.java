package com.questnr.controllers.user;

import com.questnr.model.dto.AvatarDTO;
import com.questnr.services.user.UserBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserBannerController {

    @Autowired
    UserBannerService userBannerService;

    @RequestMapping(value = "/banner", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public AvatarDTO uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return this.userBannerService.uploadBanner(file);
    }

    @RequestMapping(value = "/banner", method = RequestMethod.GET)
    public AvatarDTO getUserBanner() {
        return this.userBannerService.getUserBanner();
    }

    @RequestMapping(value = "/{userSlug}/banner", method = RequestMethod.GET)
    public AvatarDTO getUserBanner(@PathVariable String userSlug) {
        return this.userBannerService.getUserBanner(userSlug);
    }

    @RequestMapping(value = "/banner", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteFileUsingPathToFile() {
        this.userBannerService.deleteBanner();
    }
}