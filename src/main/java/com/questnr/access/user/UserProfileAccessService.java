package com.questnr.access.user;

import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.UserRepository;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileAccessService {
    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserRepository userRepository;

    public User getUserByUserSlug(String userSlug) {
        User user = userRepository.findBySlug(userSlug);
        if (user != null) {
            return user;
        }
        throw new ResourceNotFoundException("User not found!");
    }
}
