package com.questnr.services.user;

import com.questnr.common.enums.RelationShipType;
import com.questnr.exceptions.AlreadyExistsException;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.User;
import com.questnr.model.entities.UserFollower;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.model.repositories.CommunityUserRepository;
import com.questnr.model.repositories.UserFollowerRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.notification.NotificationJob;
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

    @Autowired
    NotificationJob notificationJob;

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
                userRepository.save(this.addUserToUser(userBeingFollowed, user));

                // Notification job created and assigned to Notification Processor.
                notificationJob.createNotificationJob(userFollowerRepository.findByUserAndFollowingUser(userBeingFollowed, user));

                return userBeingFollowed;
            }
            return null;
        }).orElseThrow(() -> {
            throw new ResourceNotFoundException("User being followed does not exists");
        });
        return null;
    }

    public void undoFollowUser(Long userBeingFollowedId) {
        User user = userCommonService.getUser();
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

                // Notification job created and assigned to Notification Processor.
                notificationJob.createNotificationJob(userFollowerRepository.findByUserAndFollowingUser(userBeingFollowed, user), false);

                return userRepository.save(user);
            }
            throw new InvalidRequestException("You are not following the user!");
        }).orElseThrow(() -> {
            throw new ResourceNotFoundException("User being followed does not exists");
        });
    }

    public RelationShipType getUserRelationShipWithUser(Long userId) {
        User user = userCommonService.getUser();
        User anotherUser = userCommonService.getUser(userId);
        if (userFollowerRepository.existsByUserAndFollowingUser(user, anotherUser))
            return RelationShipType.followed;
        else if (user.equals(anotherUser)) {
            return RelationShipType.owned;
        }
        return RelationShipType.none;
    }
}
