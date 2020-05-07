package com.questnr.services.user;

import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.User;
import com.questnr.model.projections.UserProjection;
import com.questnr.model.repositories.UserRepository;
import com.questnr.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
public class UserCommonService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final String dir = "u";

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

    public Page<UserProjection> searchUserString(String userString, Pageable pageable){
        try{
            return userRepository.findByUserContaining(userString, pageable);
        }catch (Exception e){
            throw new ResourceNotFoundException("User not found!");
        }
    }

    public User getUser(String userEmailId){
        User user = userRepository.findByEmailId(userEmailId);
        if (user != null) {
            return user;
        } else {
            throw new ResourceNotFoundException("Email id has not been registered!");
        }
    }

    public User getUser(Long userId){
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            return user;
        } else {
            throw new ResourceNotFoundException("User does not exists!");
        }
    }

    public User getUserByUserSlug(String userSlug) {
        User user = userRepository.findBySlug(userSlug);
        if (user != null) {
            return user;
        }
        throw new ResourceNotFoundException("User not found!");
    }
}
