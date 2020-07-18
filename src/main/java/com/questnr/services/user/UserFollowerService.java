package com.questnr.services.user;

import com.questnr.common.enums.RelationShipType;
import com.questnr.exceptions.AlreadyExistsException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.user.UserOtherDTO;
import com.questnr.model.entities.User;
import com.questnr.model.entities.UserFollower;
import com.questnr.model.mapper.UserMapper;
import com.questnr.model.repositories.*;
import com.questnr.services.CustomPageService;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.notification.NotificationJob;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    CustomPageService<User> customPageService;

    @Autowired
    NotificationJob notificationJob;

    @Autowired
    UserMapper userMapper;

    @Autowired
    NotificationRepository notificationRepository;

    UserFollowerService() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    private User addUserToUser(User userBeingFollowed, User user) {
        Set<UserFollower> userFollowers = user.getThisFollowingUserSet();
        UserFollower userFollower = new UserFollower();
        userFollower.setUser(userBeingFollowed);
        userFollower.setFollowingUser(user);
        userFollowers.add(userFollower);
        user.setThisFollowingUserSet(userFollowers);
        return userBeingFollowed;
    }

    private User addUserToUserFollowing(User user, UserFollower userFollower) {
        Set<UserFollower> userFollowers = user.getThisFollowingUserSet();
        userFollowers.add(userFollower);
        user.setThisFollowingUserSet(userFollowers);
        return user;
    }

    public boolean existsUserFollower(User userBeingFollowed, User user) {
        return userFollowerRepository.existsByUserAndFollowingUser(userBeingFollowed, user);
    }

    public boolean isFollowingUser(User userBeingFollowed, User user) {
        return userFollowerRepository.existsByUserAndFollowingUser(userBeingFollowed, user)
                || userBeingFollowed.equals(user);
    }

    public User followUser(Long userId) {
        User user = userCommonService.getUser();
        User userBeingFollowed = userRepository.findByUserId(userId);
        if (!Objects.equals(user.getUserId(), userBeingFollowed.getUserId())) {
            if (this.existsUserFollower(userBeingFollowed, user))
                throw new AlreadyExistsException("You are already following the user!");
            try {
//                userRepository.save(this.addUserToUser(userBeingFollowed, user));
                UserFollower userFollower = new UserFollower();
                userFollower.setUser(userBeingFollowed);
                userFollower.setFollowingUser(user);
                UserFollower savedUserFollower = userFollowerRepository.save(userFollower);
//                userRepository.save(this.addUserToUserFollowing(user, savedUserFollower));

                // Notification job created and assigned to Notification Processor.
                notificationJob.createNotificationJob(savedUserFollower);

//                notificationJob.createNotificationJob(userFollowerRepository.findByUserAndFollowingUser(userBeingFollowed, user));

                return userBeingFollowed;
            } catch (Exception e) {
                return user;
            }
        } else {
            throw new ResourceNotFoundException("User being followed does not exists");
        }
    }

    public void undoFollowUser(Long userId, Long userBeingFollowedId) {
        User user = userCommonService.getUser(userId);
        User userBeingFollowed = userRepository.findByUserId(userBeingFollowedId);
        UserFollower userFollower = userFollowerRepository.findByUserAndFollowingUser(userBeingFollowed, user);
        if (userFollower != null) {
            // Notification job created and assigned to Notification Processor.
            notificationJob.createNotificationJob(userFollower, false);
//            try {
//                notificationRepository.deleteByNotificationBaseAndType(
//                        userFollower.getUserFollowerId(),
//                        userFollower.getNotificationType().getJsonValue()
//                );
//            } catch (Exception e) {
//
//            }
            userFollowerRepository.delete(userFollower);
        } else {
            throw new ResourceNotFoundException("You are not following the user");
        }
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

    public Page<UserOtherDTO> getFollowersOfUser(User user, Pageable pageable) {
        Page<UserFollower> userPage = userFollowerRepository.findAllByUserOrderByCreatedAtDesc(user, pageable);
        List<User> followers = userPage.getContent().stream().map(UserFollower::getFollowingUser).collect(Collectors.toList());
        return new PageImpl<>(userMapper.toOthersDTOs(followers), pageable, userPage.getTotalElements());
    }

    public Page<UserOtherDTO> getUserFollowingToOtherUsers(User user, Pageable pageable) {
        Page<UserFollower> userPage = userFollowerRepository.findAllByFollowingUserOrderByCreatedAtDesc(user, pageable);
        List<User> followingTo = userPage.getContent().stream().map(UserFollower::getUser).collect(Collectors.toList());
        return new PageImpl<>(userMapper.toOthersDTOs(followingTo), pageable, userPage.getTotalElements());
    }
}
