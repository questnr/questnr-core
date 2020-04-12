package com.questnr.controllers;

import com.questnr.model.projections.HashTagProjection;
import com.questnr.services.HashTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1")
public class HashTagController {
    @Autowired
    HashTagService hashTagService;

    @RequestMapping(value = "/search/hash-tag/{hashTag}", method = RequestMethod.GET)
    Set<HashTagProjection> searchHashTag(@PathVariable String hashTag) {
        return hashTagService.searchHashTag(hashTag);
    }
}
