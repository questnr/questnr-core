package com.questnr.services.community;

import com.questnr.exceptions.AccessException;
import com.questnr.exceptions.AlreadyExistsException;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityInvitedUser;
import com.questnr.model.entities.CommunityUser;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.CommunityInvitedUserRepository;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.model.repositories.CommunityUserRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.services.user.UserCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

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

    private Community addUserToCommunity(Community community, User user) {
        Set<CommunityUser> communityUsers = community.getUsers();
        CommunityUser communityUser = new CommunityUser();
        communityUser.setUser(user);
        communityUser.setCommunity(community);
        communityUsers.add(communityUser);
        community.setUsers(communityUsers);
        return community;
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

    private boolean existsCommunityUser(Community community, User user) {
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
            communityRepository.save(this.addUserToCommunity(community, user));
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
            if (this.existsCommunityInvitation(community, user))
                throw new AlreadyExistsException("User have already been invited!");
            if (this.hasCommunityInvitationAccess(userCommonService.getUserId(), community.getOwnerUser().getUserId())) {
                return communityRepository.save(this.addInvitationFromCommunity(community, user));
            }
            return null;
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
                    community = this.addUserToCommunity(community, user);
                }
                return communityRepository.save(community);
            }else if(this.existsCommunityUser(community, user)){
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
            if (this.existsCommunityUser(community, user)) {
                Set<CommunityUser> joinedUsers = community.getUsers();
                for (CommunityUser communityUser : joinedUsers) {
                    if (Objects.equals(communityUser.getUser().getUserId(), user.getUserId())) {
                        joinedUsers.remove(communityUser);
                        break;
                    }
                }
                community.setUsers(joinedUsers);
                return communityRepository.save(community);
            }
            throw new InvalidRequestException("You are not member of this community");
        }).orElseThrow(() -> {
            throw new ResourceNotFoundException("Error in accepting the invitation");
        });
    }
}
