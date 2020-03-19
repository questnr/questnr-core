package com.questnr.services.user;

import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.UserRepository;
import com.questnr.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
public class UserCommonService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final String dir = "users";

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
            throw new ResourceNotFoundException("User not found!");
        }
    }

    public Long getUserId() {
        return jwtTokenUtil.getLoggedInUserID();
    }

    public String getS3BucketUserFolder() {
        return Paths.get(dir,this.getUserId().toString()).toString();
    }

    public String joinPathToFile(String fileName) {
        return Paths.get(this.getS3BucketUserFolder() ,fileName).toString();
    }
}
