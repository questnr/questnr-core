package com.questnr.controllers.user;

import com.questnr.model.dto.StaticInterestDTO;
import com.questnr.services.user.UserInterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class UserInterestController {
    @Autowired
    private UserInterestService userInterestService;

    // Get community suggestions for user first login
    @RequestMapping(value = "/user/search/interest", method = RequestMethod.GET)
    List<StaticInterestDTO> getCommunitySuggestionsForGuide(
            @RequestParam(defaultValue = "") String interestString) {
        return this.userInterestService.searchUserInterest(interestString);
    }
}
