package com.questnr.services.user;

import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.UserRepository;
import com.questnr.security.JwtTokenUtil;
import com.questnr.services.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
public class UserCommonService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final String dir = "u";

    final String USER_AVATAR_DIR = "avt";

    final String USER_BANNER_DIR = "banner";

    final String USER_COMMENT_DIR = "comment";

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private CommonService commonService;

    @Value("${amazonProperties.publicAssetPath}")
    private String publicAssetPath;

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
        return Paths.get(dir, this.getUserId().toString()).toString();
    }

    public String joinPathToFile(String fileName) {
        return Paths.get(this.getS3BucketUserFolder(), fileName).toString();
    }

    public String getAvatarPathToDir(){
        return Paths.get(this.getS3BucketUserFolder(), USER_AVATAR_DIR).toString();
    }

    public String getBannerPathToDir(){
        return Paths.get(this.getS3BucketUserFolder(), USER_BANNER_DIR).toString();
    }

    public String getCommentPathToFile(String fileName){
        return Paths.get(this.getS3BucketUserFolder(), USER_COMMENT_DIR, fileName).toString();
    }

    public String getBannerPathToFile(String fileName){
        return Paths.get(publicAssetPath, this.getS3BucketUserFolder(), USER_BANNER_DIR, fileName).toString();
    }

    public String getAvatarPathToFile(String fileName){
//        Path p = Paths.get("Brijesh/brij");
//        Iterator<Path> ps = p.iterator();
//        while(ps.hasNext()){
//            System.out.println(ps.next());
//        }
        return Paths.get(publicAssetPath, this.getS3BucketUserFolder(), USER_AVATAR_DIR, fileName).toString();
    }

    public Page<User> searchUserString(String userString, Pageable pageable) {
        try {
            userString = userString.toLowerCase();
            return userRepository.findByUserContaining(userString, pageable);
        } catch (Exception e) {
            throw new ResourceNotFoundException("User not found!");
        }
    }

    public User getUser(String userEmailId) {
        User user = userRepository.findByEmailId(userEmailId);
        if (user != null) {
            return user;
        } else {
            throw new ResourceNotFoundException("Email id has not been registered!");
        }
    }

    public User getUser(Long userId) {
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
