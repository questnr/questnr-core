package com.questnr.services.user;

import com.questnr.model.dto.StaticInterestDTO;
import com.questnr.model.entities.EntityTag;
import com.questnr.model.entities.StaticInterest;
import com.questnr.model.entities.User;
import com.questnr.model.entities.UserInterest;
import com.questnr.model.repositories.StaticInterestRepository;
import com.questnr.model.repositories.UserInterestRepository;
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
        User user = userCommonService.getUser();
        List<String> userInterests = communityTagService.parseCommunityTags(
                communityTagService.getCommunityTags(userInterestsRequest.getUserInterests(), true));
        for (String userInterestString : userInterests) {
            try {
                EntityTag entityTag =
                        entityTagService.saveEntityTag(userInterestString.toLowerCase());
                if (entityTag != null && !CommonService.isNull(entityTag.getTagValue())) {
                    UserInterest userInterest = new UserInterest();
                    userInterest.setEntityTag(entityTag);
                    userInterest.setUser(user);
                    this.userInterestRepository.save(userInterest);
                }
            } catch (Exception e) {
                LOGGER.error("storeUserInterests: ERROR, userID: " + user.getUserId() + "," +
                        " " + userInterestString);
            }
        }
    }
}
