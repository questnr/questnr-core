package com.questnr.services.community;

import com.questnr.access.CommunityAccessService;
import com.questnr.common.enums.RelationShipType;
import com.questnr.exceptions.AccessException;
import com.questnr.exceptions.AlreadyExistsException;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.community.CommunityDTO;
import com.questnr.model.dto.user.UserOtherDTO;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityInvitedUser;
import com.questnr.model.entities.CommunityUser;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.model.mapper.UserMapper;
import com.questnr.model.repositories.*;
import com.questnr.services.notification.NotificationJob;
import com.questnr.services.user.UserCommonService;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommunityJoinService {
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
    CommunityInvitedUserRepository communityInvitedUserRepository;

    @Autowired
    NotificationJob notificationJob;

    @Autowired
    CommunityMapper communityMapper;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserFollowerRepository userFollowerRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CommunityAccessService communityAccessService;

    CommunityJoinService() {
        communityMapper = Mappers.getMapper(CommunityMapper.class);
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    public Page<CommunityDTO> getJoinedCommunityList(User user, Pageable pageable) {
        Page<CommunityUser> communityUserPage = communityUserRepository.findAllByUserOrderByCreatedAtDesc(user, pageable);
        List<Community> joinedCommunityList = communityUserPage.getContent().stream().map(CommunityUser::getCommunity).collect(Collectors.toList());
        return new PageImpl<>(communityMapper.toDTOs(joinedCommunityList), pageable, communityUserPage.getTotalElements());
    }

    public Page<CommunityDTO> getCommunityInvitationList(Pageable pageable) {
        Page<CommunityInvitedUser> communityInvitedUserPage = communityInvitedUserRepository.findAllByUserOrderByCreatedAtDesc(userCommonService.getUser(), pageable);
        List<Community> invitedCommunityList = communityInvitedUserPage.getContent().stream().map(CommunityInvitedUser::getCommunity).collect(Collectors.toList());
        return new PageImpl<>(communityMapper.toDTOs(invitedCommunityList), pageable, communityInvitedUserPage.getTotalElements());
    }
//
//    private Community addUserToCommunity(Community community, User user) {
//        Set<CommunityUser> communityUsers = community.getUsers();
//        CommunityUser communityUser = new CommunityUser();
//        communityUser.setUser(user);
//        communityUser.setCommunity(community);
//        communityUsers.add(communityUser);
//        community.setUsers(communityUsers);
//        return community;
//    }

    private CommunityUser createCommunityUser(Community community, User user) {
        CommunityUser communityUser = new CommunityUser();
        communityUser.setUser(user);
        communityUser.setCommunity(community);
        return communityUserRepository.save(communityUser);
    }

    private Community addInvitationFromCommunity(Community community, User user) {
        User userActor = userCommonService.getUser();
        Set<CommunityInvitedUser> invitedUsers = community.getInvitedUsers();
        CommunityInvitedUser communityInvitedUser = new CommunityInvitedUser();
        communityInvitedUser.setCommunity(community);
        communityInvitedUser.setUser(user);
        communityInvitedUser.setUserActor(userActor);
        invitedUsers.add(communityInvitedUser);
        community.setInvitedUsers(invitedUsers);
        return community;
    }

    private Community removeThisUserFromInvitationList(Community community, User user) {
        Set<CommunityInvitedUser> invitedUsers = community.getInvitedUsers();
        for (CommunityInvitedUser communityInvitedUser : invitedUsers) {
            if (Objects.equals(communityInvitedUser.getUser().getUserId(), user.getUserId())) {
                invitedUsers.remove(communityInvitedUser);
                break;
            }
        }
        community.setInvitedUsers(invitedUsers);
        return community;
    }

    public boolean existsCommunityUser(Community community, User user) {
        return communityUserRepository.existsByCommunityAndUser(community, user);
    }

    private boolean existsCommunityInvitation(Community community, User user) {
        return communityInvitedUserRepository.existsByCommunityAndUser(community, user);
    }

    private boolean hasCommunityInvitationAccess(Long userId, Long communityOwnerId) {
        if (Objects.equals(userId, communityOwnerId)) return true;
        throw new AccessException("You don not have access to send an invitation.");
    }

    public Community joinCommunity(Long communityId) {
        communityRepository.findById(communityId).map(community -> {
            User user = userCommonService.getUser();
            if (this.existsCommunityUser(community, user) || community.getOwnerUser().equals(user))
                throw new AlreadyExistsException("You are already been joined!");
//            communityRepository.save(this.addUserToCommunity(community, user));

            CommunityUser savedCommunityUser = this.createCommunityUser(community, user);
            // Notification job created and assigned to Notification Processor.
            notificationJob.createNotificationJob(savedCommunityUser);

            return community;
        }).orElseThrow(() -> {
            throw new ResourceNotFoundException("Error in joining community");
        });
        return null;
    }

    private void inviteUserToJoinCommunity(Long communityId, User user) {
        communityRepository.findById(communityId).map(community -> {
            if (this.existsCommunityUser(community, user) || community.getOwnerUser().equals(user))
                throw new AlreadyExistsException("User is already member!");
//            if (this.existsCommunityInvitation(community, user))
//                throw new AlreadyExistsException("User have already been invited!");
            if (this.hasCommunityInvitationAccess(userCommonService.getUserId(), community.getOwnerUser().getUserId())) {
                communityRepository.save(this.addInvitationFromCommunity(community, user));

                // Notification job created and assigned to Notification Processor.
                notificationJob.createNotificationJob(communityInvitedUserRepository.findFirstByCommunityAndUser(community, user));
            }
            return community;
        }).orElseThrow(() -> {
            throw new ResourceNotFoundException("Error in inviting the user");
        });
    }

    public void inviteUserToJoinCommunity(Long communityId, Long userId) {
        this.inviteUserToJoinCommunity(communityId, userCommonService.getUser(userId));
    }

    public void inviteUserToJoinCommunity(Long communityId, String userEmail) {
        this.inviteUserToJoinCommunity(communityId, userCommonService.getUser(userEmail));
    }

    private void actionOnInvitationFromCommunity(Long communityId, User user, boolean hasAccepted) {
        communityRepository.findById(communityId).map(community -> {
            if (!this.existsCommunityUser(community, user) && this.existsCommunityInvitation(community, user)) {
                community = this.removeThisUserFromInvitationList(community, user);
                if (hasAccepted) {
//                    community = this.addUserToCommunity(community, user);
                    this.createCommunityUser(community, user);
                }
//                return communityRepository.save(community);
                return community;
            } else if (this.existsCommunityUser(community, user)) {
                throw new AlreadyExistsException("You have already been joined");
            }
            throw new InvalidRequestException("You don't have invitation from this community");
        }).orElseThrow(() -> {
            throw new ResourceNotFoundException("Error in accepting the invitation");
        });
    }

    public void actionOnInvitationFromCommunity(Long communityId, boolean hasAccepted) {
        this.actionOnInvitationFromCommunity(communityId, userCommonService.getUser(), hasAccepted);
    }

    public void actionOnInvitationFromCommunity(Long communityId, String userEmail, boolean hasAccepted) {
        this.actionOnInvitationFromCommunity(communityId, userCommonService.getUser(userEmail), hasAccepted);
    }

    public void revokeJoinFromUser(Long communityId, Long userId) {
        User user = userCommonService.getUser(userId);
        communityRepository.findById(communityId).map(community -> {
            CommunityUser communityUser = communityUserRepository.findByCommunityAndUser(community, user);
            if (communityUser != null) {

                // Notification job created and assigned to Notification Processor.
//                notificationJob.createNotificationJob(thisCommunityUser, false);
                try {
                    notificationRepository.deleteByNotificationBaseAndType(
                            communityUser.getCommunityUserId(),
                            communityUser.getNotificationType().getJsonValue()
                    );
                } catch (Exception e) {

                }
                communityUserRepository.delete(communityUser);
                return community;
            } else {
                throw new InvalidRequestException("You are not member of this community");
            }
        }).orElseThrow(() -> {
            throw new ResourceNotFoundException("Error in accepting the invitation");
        });
    }

    public RelationShipType getUserRelationShipWithCommunity(Long communityId) {
        User user = userCommonService.getUser();
        Community community = communityCommonService.getCommunity(communityId);
        if (community.getOwnerUser().equals(user)) {
            return RelationShipType.owned;
        } else if (communityUserRepository.existsByCommunityAndUser(community, user)) {
            return RelationShipType.followed;
        }
        return RelationShipType.none;
    }

    public Page<UserOtherDTO> getUserListToInvite(CommunityUser communityUser, Pageable pageable) {
//        Page<User> userPage = this.communityUserRepository.getAllByCommunityAndInviteUser(
//                communityUser.getCommunity(),
//                communityUser.getUser(),
//                pageable);
//        List<User> userList = communityUserPage.getContent().stream().map(CommunityUser::getUser).collect(Collectors.toList());

        Page<User> userPage = this.userFollowerRepository.getAllByUser(communityUser.getUser(), PageRequest.of(0, Integer.MAX_VALUE));

        List<User> userList = userPage.getContent();
        List<User> filteredUserList = userList.stream().filter(user ->
                !this.communityCommonService.isUserMemberOfCommunity(user, communityUser.getCommunity())
        ).collect(Collectors.toList());

        List<User> pagedUserList = new ArrayList<>();

        if (pageable.getPageNumber() * pageable.getPageSize() < filteredUserList.size()) {
            pagedUserList = filteredUserList.subList(pageable.getPageNumber() * pageable.getPageSize(),
                    Math.min(
                            (pageable.getPageNumber() + 1) * pageable.getPageSize(),
                            filteredUserList.size()
                    ));
        }

        return new PageImpl<>(userMapper.toOthersDTOs(pagedUserList),
                pageable,
                filteredUserList.size());
    }
}
