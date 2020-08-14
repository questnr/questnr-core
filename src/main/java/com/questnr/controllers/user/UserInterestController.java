package com.questnr.controllers.user;

import com.questnr.common.enums.CommunitySuggestionDialogActionType;
import com.questnr.model.dto.StaticInterestDTO;
import com.questnr.requests.UserInterestsRequest;
import com.questnr.services.user.UserInterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    // Store user interests
    @RequestMapping(value = "/user/interest", method = RequestMethod.POST)
    void storeUserInterests(@RequestBody UserInterestsRequest userInterestsRequest){
        this.userInterestService.storeUserInterests(userInterestsRequest);
    }

    // User Skips Suggestion dialog
    @RequestMapping(value = "/user/interest", method = RequestMethod.DELETE)
    void storeUserInterests(){
        this.userInterestService.communitySuggestionDialogAction(CommunitySuggestionDialogActionType.skipped);
    }
}
