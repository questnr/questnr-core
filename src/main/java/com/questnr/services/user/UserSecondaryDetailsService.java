package com.questnr.services.user;

import com.questnr.model.entities.UserSecondaryDetails;
import com.questnr.model.repositories.UserSecondaryDetailsRepository;
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
    private UserSecondaryDetailsRepository userSecondaryDetailsRepository;

    public UserSecondaryDetails getUserSecondaryDetails(){
        return userSecondaryDetailsRepository.findFirstByUser(userCommonService.getUser());
    }
}
