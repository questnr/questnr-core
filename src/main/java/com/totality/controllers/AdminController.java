package com.totality.controllers;

import com.totality.model.entities.Community;
import com.totality.model.entities.Post;
import com.totality.requests.CommunityRequests;
import com.totality.responses.CommunityResponse;
import com.totality.services.BaseService;
import com.totality.services.CreateUpdateDelete;
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
  CreateUpdateDelete service;

  @Autowired
  BaseService baseService;

  @RequestMapping(value = "/create-community", method = RequestMethod.PUT)
  Boolean signup(@RequestBody Community requests) {
    Boolean response = service.createCommunity(requests);
    return response;
  }

  @RequestMapping(value = "/create-post", method = RequestMethod.PUT)
  Boolean signup(@RequestBody Post requests) {
    Boolean response = service.creatPost(requests);
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
