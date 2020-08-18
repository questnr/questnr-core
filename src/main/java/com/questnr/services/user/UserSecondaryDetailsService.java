package com.questnr.services.user;

import com.questnr.model.entities.User;
import com.questnr.model.entities.UserSecondaryDetails;
import com.questnr.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSecondaryDetailsService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserRepository userRepository;

    public UserSecondaryDetails getUserSecondaryDetails() {
        User user = userCommonService.getUser();
        if (user.getUserSecondaryDetails() == null
                || user.getUserSecondaryDetails().getLoggedInCount() == null) {
            return this.createAndSaveUserSecondaryDetails(user);
        } else {
            return user.getUserSecondaryDetails();
        }
    }

    public UserSecondaryDetails createUserSecondaryDetails(User user) {
        try {
            UserSecondaryDetails userSecondaryDetails = new UserSecondaryDetails();
            userSecondaryDetails.defaultData();
            userSecondaryDetails.setUser(user);
            userSecondaryDetails.addMetadata();
            user.setUserSecondaryDetails(userSecondaryDetails);
            return userSecondaryDetails;
        } catch (Exception e) {
            LOGGER.error("Error while creating UserSecondaryDetails for " + user.getUserId());
            return null;
        }
    }

    public UserSecondaryDetails createAndSaveUserSecondaryDetails(User user) {
        try {
            UserSecondaryDetails userSecondaryDetails = this.createUserSecondaryDetails(user);
            user.setUserSecondaryDetails(userSecondaryDetails);
            userRepository.save(user);
            return userSecondaryDetails;
        } catch (Exception e) {
            LOGGER.error("Error while saving UserSecondaryDetails for " + user.getUserId());
            return null;
        }
    }
}
