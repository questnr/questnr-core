package com.questnr.services;

import com.questnr.exceptions.InvalidInputException;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.UserRepository;
import com.questnr.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommonUserService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${questNRProperties.pathSeparator}")
    private String pathSeparator;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserRepository userRepository;

    public User getUser() {
        long userId = jwtTokenUtil.getLoggedInUserID();
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            return user;
        } else {
            LOGGER.error(User.class.getName() + " Exception Occurred");
            throw new InvalidInputException(User.class.getName(), null, null);
        }
    }

    public Long getUserId() {
        return jwtTokenUtil.getLoggedInUserID();
    }

    public String getS3BucketUserFolder() {
        return this.getUserId().toString();
    }

    public String joinPathToFile(String fileName) {
        return this.getS3BucketUserFolder() + "" + this.pathSeparator + "" + fileName;
    }
}
