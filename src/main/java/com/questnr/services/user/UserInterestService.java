package com.questnr.services.user;

import com.questnr.common.enums.CommunitySuggestionDialogActionType;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.model.dto.StaticInterestDTO;
import com.questnr.model.entities.*;
import com.questnr.model.repositories.StaticInterestRepository;
import com.questnr.model.repositories.UserInterestRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.model.repositories.UserSecondaryDetailsRepository;
import com.questnr.requests.UserInterestsRequest;
import com.questnr.services.CommonService;
import com.questnr.services.EntityTagService;
import com.questnr.services.community.CommunityTagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserInterestService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private StaticInterestRepository staticInterestRepository;

    @Autowired
    private UserInterestRepository userInterestRepository;

    @Autowired
    private CommunityTagService communityTagService;

    @Autowired
    private UserCommonService userCommonService;

    @Autowired
    private EntityTagService entityTagService;

    @Autowired
    private UserSecondaryDetailsRepository userSecondaryDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    public List<StaticInterestDTO> searchUserInterest(String interestString) {
        Pageable pageable = PageRequest.of(0, 6);
        try {
            Page<StaticInterest> staticInterestPage = staticInterestRepository
                    .findByInterestContaining(interestString.toLowerCase(), pageable);
            return staticInterestPage.stream().map(StaticInterestDTO::new).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(UserInterestService.class.getName() + ": Error in saving EntityTag");
        }
        return null;
    }

    public void storeUserInterests(UserInterestsRequest userInterestsRequest) {
        List<String> userInterests = communityTagService.parseCommunityTags(
                communityTagService.getCommunityTags(userInterestsRequest.getUserInterests(), true));
        this.storeUserInterests(userInterests);
    }

    public void storeUserInterests(List<String> userInterests) {
        User user = userCommonService.getUser();
        this.communitySuggestionDialogAction(user, CommunitySuggestionDialogActionType.completed);
        for (String userInterestString : userInterests) {
            try {
                EntityTag entityTag =
                        entityTagService.saveEntityTag(userInterestString.toLowerCase());
                if (entityTag != null && !CommonService.isNull(entityTag.getTagValue())) {
                    if(!this.userInterestRepository.existsByEntityTagAndUser(entityTag, user)) {
                        UserInterest userInterest = new UserInterest();
                        userInterest.setEntityTag(entityTag);
                        userInterest.setUser(user);
                        this.userInterestRepository.save(userInterest);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("storeUserInterests: ERROR, userID: " + user.getUserId() + "," +
                        " " + userInterestString);
            }
        }
    }

    public void communitySuggestionDialogAction(User user,
                                                CommunitySuggestionDialogActionType communitySuggestionDialogActionType){
        try{
            UserSecondaryDetails userSecondaryDetails = new UserSecondaryDetails();
            if(user.getUserSecondaryDetails() == null
                    || user.getUserSecondaryDetails().getLoggedInCount() == null){
                UserSecondaryDetails newUserSecondaryDetails = new UserSecondaryDetails();
                newUserSecondaryDetails.defaultData();
                newUserSecondaryDetails.setCommunitySuggestion(communitySuggestionDialogActionType);
                newUserSecondaryDetails.setUser(user);
                newUserSecondaryDetails.addMetadata();
                user.setUserSecondaryDetails(newUserSecondaryDetails);
                userRepository.save(user);
            }
            else{
                userSecondaryDetails = user.getUserSecondaryDetails();
                userSecondaryDetails.setCommunitySuggestion(communitySuggestionDialogActionType);
                userSecondaryDetails.updateMetadata();
                user.setUserSecondaryDetails(userSecondaryDetails);
                userRepository.save(user);
            }

        }catch (Exception e){
            throw new InvalidRequestException("Something went wrong!");
        }
    }

    public void communitySuggestionDialogAction(CommunitySuggestionDialogActionType communitySuggestionDialogActionType){
        User user = userCommonService.getUser();
        this.communitySuggestionDialogAction(user, communitySuggestionDialogActionType);
    }
}
