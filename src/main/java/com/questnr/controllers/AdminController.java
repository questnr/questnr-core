package com.questnr.controllers;

import com.questnr.model.entities.Community;
import com.questnr.services.BaseService;
import com.questnr.services.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class AdminController {

  @Autowired
  CommunityService service;

  @Autowired
  BaseService baseService;

  @RequestMapping(value = "/create-community", method = RequestMethod.PUT)
  Boolean signup(@RequestBody Community requests) {
    Boolean response = service.createCommunity(requests);
    return response;
  }

  @RequestMapping(value = "slug/{slug}/type/{slug_type}/exist", method = RequestMethod.GET)
  @ResponseBody
  public boolean slugExistence(@PathVariable("slug") String slug,
      @PathVariable("slug_type") String type) {
    boolean b = baseService.slugExistence(slug, type);
    return b;
  }

}
