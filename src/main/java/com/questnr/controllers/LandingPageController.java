package com.questnr.controllers;

import com.questnr.model.dto.HashTagWithRankDTO;
import com.questnr.model.dto.UserWithRankDTO;
import com.questnr.services.LandingPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class LandingPageController {

    @Autowired
    LandingPageService landingPageService;

    @RequestMapping(value = "/users-with-highest-rank", method = RequestMethod.GET)
    Page<UserWithRankDTO> getUsersWithHighestRank(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.landingPageService.getUsersWithHighestRank(pageable);
    }

    @RequestMapping(value = "/hash-tag-with-highest-rank", method = RequestMethod.GET)
    Page<HashTagWithRankDTO> getHashtagsWithHighestRank(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return this.landingPageService.getHashTagsWithHighestRank(pageable);
    }
}
