package com.questnr.services.user;

import com.questnr.exceptions.AlreadyExistsException;
import com.questnr.exceptions.DoesNotExistsException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.User;
import com.questnr.model.entities.UserFollower;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.model.repositories.CommunityUserRepository;
import com.questnr.model.repositories.UserFollowerRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.services.community.CommunityCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Set;

@Transactional
@Service
public class UserFollowerService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommunityUserRepository communityUserRepository;

    @Autowired
    UserFollowerRepository userFollowerRepository;

    private User addUserToUser(User userBeingFollowed, User user) {
        Set<UserFollower> userFollowers = user.getThisFollowingUserSet();
        UserFollower userFollower = new UserFollower();
        userFollower.setUser(userBeingFollowed);
        userFollower.setFollowingUser(user);
        userFollowers.add(userFollower);
        user.setThisFollowingUserSet(userFollowers);
        return userBeingFollowed;
    }

    private boolean existsUserFollower(User userBeingFollowed, User user) {
        return userFollowerRepository.existsByUserAndFollowingUser(userBeingFollowed, user);
    }

    public User followUser(Long userId) {
        userRepository.findById(userId).map(userBeingFollowed -> {
            User user = userCommonService.getUser();
            if (!Objects.equals(user.getUserId(), userBeingFollowed.getUserId())) {
               if (this.existsUserFollower(userBeingFollowed, user))
                    throw new AlreadyExistsException("You are already following the user!");
                return userRepository.save(this.addUserToUser(userBeingFollowed, user));
            }
            return null;
        }).orElseThrow(() -> {
            throw new ResourceNotFoundException("User being followed does not exists");
        });
        return null;
    }

    public void undoFollowUser(Long userBeingFollowedId, Long userId) {
        User user = userCommonService.getUser(userId);
        userRepository.findById(userBeingFollowedId).map(userBeingFollowed -> {
            if (this.existsUserFollower(userBeingFollowed, user)) {
                Set<UserFollower> userFollowers = user.getThisFollowingUserSet();
                for (UserFollower userFollower : userFollowers) {
                    if (Objects.equals(userFollower.getFollowingUser().getUserId(), user.getUserId()) && Objects.equals(userFollower.getUser().getUserId(), userBeingFollowed.getUserId())) {
                        userFollowers.remove(userFollower);
                        break;
                    }
                }
                user.setThisFollowingUserSet(userFollowers);
                return userRepository.save(user);
            }
            throw new DoesNotExistsException("You are not following the user!");
        }).orElseThrow(()->{
            throw new ResourceNotFoundException("User being followed does not exists");
        });
    }
}
