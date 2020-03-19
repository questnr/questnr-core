package com.questnr.controllers;

import com.questnr.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class AdminController {

    @Autowired
    AdminService adminService;

    @RequestMapping(value = "slug/{slug}/type/{slug_type}/exist", method = RequestMethod.GET)
    @ResponseBody
    public boolean slugExistence(@PathVariable("slug") String slug,
                                 @PathVariable("slug_type") String type) {
        return adminService.slugExistence(slug, type);
    }
}
