package com.questnr.controllers;

import com.questnr.model.projections.HashTagProjection;
import com.questnr.services.HashTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1")
public class HashTagController {
    @Autowired
    HashTagService hashTagService;

    @RequestMapping(value = "/search/hash-tag", method = RequestMethod.GET)
    Set<HashTagProjection> searchHashTag(@RequestParam String hashTag) {
        return hashTagService.searchHashTag(hashTag);
    }
}
