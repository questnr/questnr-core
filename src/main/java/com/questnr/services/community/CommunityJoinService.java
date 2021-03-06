package com.questnr.services.community;

import com.questnr.access.community.CommunityAccessService;
import com.questnr.common.enums.CommunityPrivacy;
import com.questnr.common.enums.NotificationType;
import com.questnr.common.enums.RelationShipType;
import com.questnr.exceptions.AccessException;
import com.questnr.exceptions.AlreadyExistsException;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.community.CommunityDTO;
import com.questnr.model.dto.user.UserOtherDTO;
import com.questnr.model.entities.*;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.model.mapper.UserMapper;
import com.questnr.model.repositories.*;
import com.questnr.responses.CommunityJoinResponse;
import com.questnr.responses.CommunityMetaProfileResponse;
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

    @Autowired
    CommunityUserRequestRepository communityUserRequestRepository;

    @Autowired
    CommunityProfileService communityProfileService;

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
        return this.createCommunityUser(community, user, NotificationType.followedCommunity);
    }

    private CommunityUser createCommunityUser(Community community, User user, NotificationType notificationType) {
        if (this.existsCommunityUser(community, user) || community.getOwnerUser().equals(user))
            throw new AlreadyExistsException("You are already been joined!");
        else {
            CommunityUser communityUser = new CommunityUser();
            communityUser.setUser(user);
            communityUser.setCommunity(community);
            communityUser.setNotificationType(notificationType);
            return communityUserRepository.save(communityUser);
        }
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

    public CommunityJoinResponse joinCommunity(Long communityId) {
        User user = userCommonService.getUser();
        Community community = communityCommonService.getCommunity(communityId);
        CommunityJoinResponse communityJoinResponse = new CommunityJoinResponse();
        communityJoinResponse.setCommunityPrivacy(community.getCommunityPrivacy());
        if (community.getCommunityPrivacy() == CommunityPrivacy.pri) {
            if (communityUserRequestRepository.existsByCommunityAndUser(community, user)) {
                throw new AlreadyExistsException("Request has already been sent");
            } else {
                CommunityUserRequest communityUserRequest = new CommunityUserRequest();
                communityUserRequest.setCommunity(community);
                communityUserRequest.setUser(user);
                CommunityUserRequest savedCommunityUserRequest = communityUserRequestRepository.save(communityUserRequest);
                communityJoinResponse.setRelationShipType(RelationShipType.requested);
                // Notification job created and assigned to Notification Processor.
                notificationJob.createNotificationJob(savedCommunityUserRequest);
            }
        } else {
            CommunityUser savedCommunityUser = this.createCommunityUser(community, user);
            communityJoinResponse.setRelationShipType(RelationShipType.followed);
            // Notification job created and assigned to Notification Processor.
            notificationJob.createNotificationJob(savedCommunityUser);
        }
        return communityJoinResponse;
    }

    private void inviteUserToJoinCommunity(Long communityId, User user) {
        Community community = communityCommonService.getCommunity(communityId);
        if (this.existsCommunityUser(community, user) || community.getOwnerUser().equals(user))
            throw new AlreadyExistsException("User is already member!");
//            if (this.existsCommunityInvitation(community, user))
//                throw new AlreadyExistsException("User have already been invited!");
        if (this.hasCommunityInvitationAccess(userCommonService.getUserId(), community.getOwnerUser().getUserId())) {
            communityRepository.save(this.addInvitationFromCommunity(community, user));

            // Notification job created and assigned to Notification Processor.
            notificationJob.createNotificationJob(communityInvitedUserRepository.findFirstByCommunityAndUser(community, user));
        }
    }

    public void inviteUserToJoinCommunity(Long communityId, Long userId) {
        this.inviteUserToJoinCommunity(communityId, userCommonService.getUser(userId));
    }

    public void inviteUserToJoinCommunity(Long communityId, String userEmail) {
        this.inviteUserToJoinCommunity(communityId, userCommonService.getUser(userEmail));
    }

    private void actionOnInvitationFromCommunity(Long communityId, User user, boolean hasAccepted) {
        Community community = communityCommonService.getCommunity(communityId);
        if (this.existsCommunityInvitation(community, user)) {
            community = this.removeThisUserFromInvitationList(community, user);
            if (hasAccepted) {
//                    community = this.addUserToCommunity(community, user);
                this.createCommunityUser(community, user);
            }
//                return communityRepository.save(community);
        } else {
            throw new InvalidRequestException("You don't have invitation from this community");
        }
    }

    public void actionOnInvitationFromCommunity(Long communityId, boolean hasAccepted) {
        this.actionOnInvitationFromCommunity(communityId, userCommonService.getUser(), hasAccepted);
    }

    public void actionOnInvitationFromCommunity(Long communityId, String userEmail, boolean hasAccepted) {
        this.actionOnInvitationFromCommunity(communityId, userCommonService.getUser(userEmail), hasAccepted);
    }

    public CommunityMetaProfileResponse revokeJoinFromUser(Long communityId, Long userId) {
        User user = userCommonService.getUser(userId);
        Community community = communityCommonService.getCommunity(communityId);
        CommunityUser communityUser = communityUserRepository.findByCommunityAndUser(community, user);
        if (communityUser != null) {
            // Notification job created and assigned to Notification Processor.
//            notificationJob.createNotificationJob(thisCommunityUser, false);
            try {
                notificationRepository.deleteByNotificationBaseAndType(
                        communityUser.getCommunityUserId(),
                        communityUser.getNotificationType().getJsonValue()
                );
            } catch (Exception e) {
            }
            communityUserRepository.delete(communityUser);
            return communityProfileService.getCommunityProfileDetails(community, "followers");
        } else {
            throw new InvalidRequestException("You are not member of this community");
        }
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

    public Page<UserOtherDTO> getCommunityUserRequestList(Long communityId, Pageable pageable) {
        Community community = communityCommonService.getCommunity(communityId);
        Page<CommunityUserRequest> communityUserRequestPage = communityUserRequestRepository.findByCommunity(community, pageable);
        List<User> userList = communityUserRequestPage.getContent().stream().map(CommunityUserRequest::getUser).collect(Collectors.toList());
        return new PageImpl<>(userMapper.toOthersDTOs(userList), pageable, communityUserRequestPage.getTotalElements());
    }

    public void actionOnCommunityUserRequest(Long communityId, Long userId, boolean shouldAccept) {
        User user = userCommonService.getUser(userId);
        Community community = communityCommonService.getCommunity(communityId);
        this.actionOnCommunityUserRequest(community, user, shouldAccept);
    }

    public void actionOnCommunityUserRequest(CommunityUserRequest communityUserRequest, boolean shouldAccept) {
        this.actionOnCommunityUserRequest(communityUserRequest.getCommunity(), communityUserRequest.getUser(), shouldAccept);
    }

    public void cancelUserOwnedRequest(Long communityId) {
        User user = userCommonService.getUser();
        Community community = communityCommonService.getCommunity(communityId);
        this.actionOnCommunityUserRequest(community, user, false);
    }

    public void actionOnCommunityUserRequest(Community community, User user, boolean shouldAccept) {
        CommunityUserRequest communityUserRequest = communityUserRequestRepository.findByCommunityAndUser(community, user);
        if (communityUserRequest != null) {
            communityUserRequestRepository.delete(communityUserRequest);
            try {
                notificationRepository.deleteByNotificationBaseAndType(
                        communityUserRequest.getCommunityUserRequestId(),
                        communityUserRequest.getNotificationType().getJsonValue()
                );
            } catch (Exception e) {

            }
            if (shouldAccept) {
                CommunityUser savedCommunityUser = this.createCommunityUser(community, user,
                        NotificationType.communityAccepted);
                // Notification job created and assigned to Notification Processor.
                notificationJob.createNotificationJob(savedCommunityUser);
            }
        } else {
            throw new ResourceNotFoundException("Community user request doesn't exists");
        }
    }

    public void actionOnAllCommunityUserRequest(Community community, boolean shouldAccept) {
        List<CommunityUserRequest> communityUserRequestList = communityUserRequestRepository.findByCommunity(community);
        for (CommunityUserRequest communityUserRequest : communityUserRequestList) {
            this.actionOnCommunityUserRequest(communityUserRequest, shouldAccept);
        }
    }
}
