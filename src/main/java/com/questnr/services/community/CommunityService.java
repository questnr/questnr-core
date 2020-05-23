package com.questnr.services.community;

import com.questnr.common.enums.PublishStatus;
import com.questnr.exceptions.AlreadyExistsException;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.UserDTO;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityUser;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.UserMapper;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.model.repositories.CommunityUserRepository;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonService;
import com.questnr.services.CustomPageService;
import com.questnr.services.user.UserCommonService;
import com.questnr.util.SecureRandomService;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunityService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Autowired
    private CommonService commonService;

    @Autowired
    private CommunityCommonService communityCommonService;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private UserCommonService userCommonService;

    @Autowired
    private CommunityAvatarService communityAvatarService;

    @Autowired
    private SecureRandomService secureRandomService;

    @Autowired
    private CustomPageService<User> customPageService;

    @Value("${questnr.domain}")
    private String QUEST_NR_DOMAIN;

    @Autowired
    private CommunityUserRepository communityUserRepository;

    @Autowired
    private UserMapper userMapper;

    public CommunityService() {
        this.userMapper = Mappers.getMapper(UserMapper.class);
    }

    final private String COMMUNITY_PATH = "community";

    private String createCommunitySlug(Community community) {
        List<String> communityNameChunks = Arrays.asList(community.getCommunityName().toLowerCase().split("\\s"));
        String randomString = secureRandomService.getSecureRandom().toString();
        return CommonService.removeSpecialCharacters(String.join("-", communityNameChunks)) +
                "-" +
                randomString;
    }

    private String getCommunityTags(Community community) {
        List<String> descChunks = Arrays.asList(community.getDescription().toLowerCase().split("\\s"));
        return CommonService.removeSpecialCharacters(String.join(", ", descChunks.subList(0, descChunks.size())));
    }

    public Community getCommunityByCommunityName(String communityName) {
        Community community = communityRepository.findByCommunityName(communityName);
        if (community != null) {
            return community;
        }
        throw new ResourceNotFoundException("Community not found!");
    }

    public boolean checkCommunityNameExists(String communityName) throws AlreadyExistsException {
        if (communityRepository.countByCommunityName(communityName) == 0) {
            return true;
        }
        throw new AlreadyExistsException("Community already exists");
    }

    public Community createCommunity(Community community, MultipartFile multipartFile) {
        Community communitySaved = this.createCommunity(community);
        try {
            if (!multipartFile.isEmpty()) {
                communityAvatarService.uploadAvatar(communitySaved.getCommunityId(), multipartFile);
                return communityCommonService.getCommunity(communitySaved.getCommunityId());
            }
            return communitySaved;
        } catch (Exception e) {
            LOGGER.error(CommunityService.class.getName() + " Exception Occurred");
        }
        return null;
    }

    public Community createCommunity(Community community) {
        if (community != null) {
            if (this.checkCommunityNameExists(community.getCommunityName())) {
                try {
                    community.setOwnerUser(userCommonService.getUser());
                    community.addMetadata();
                    community.setSlug(this.createCommunitySlug(community));
                    community.setTags(this.getCommunityTags(community));
                    return communityRepository.saveAndFlush(community);
                } catch (Exception e) {
                    LOGGER.error(CommunityService.class.getName() + " Exception Occurred");
                }
            }
        }
        throw new InvalidRequestException("Error occurred. Please, try again!");
    }

    public Page<Community> getCommunityListOfUser(User user, Pageable pageable) {
        return communityRepository.findByOwnerUser(user, pageable);
    }

    public void deleteCommunity(long communityId) {
        Community community = communityCommonService.getCommunity(communityId);
        try {
            if (!commonService.isNull(community.getAvatar().getAvatarKey())) {
                try {
                    this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(community.getAvatar().getAvatarKey());
                } catch (Exception e) {
                    LOGGER.error(CommunityService.class.getName() + " Exception Occurred. Couldn't able to delete resources of community on the cloud.");
                }
            }
            communityRepository.delete(community);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Community not Found!");
        }
    }

    public List<Community> findAllCommunityNames() {
        try {
            return communityRepository.findAllByStatus(PublishStatus.publish);
        } catch (Exception e) {
            LOGGER.error(CommunityService.class.getName() + " Exception Occurred");
            e.printStackTrace();
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public Page<UserDTO> getUsersOfCommunity(String communitySlug, Pageable pageable) {
        try {
            Page<CommunityUser> communityUserPage = communityUserRepository.findAllByCommunityOrderByCreatedAtDesc(
                    communityCommonService.getCommunity(communitySlug),
                    pageable);
            List<User> users = communityUserPage.getContent().stream()
                    .map(CommunityUser::getUser).collect(Collectors.toList());

            return new PageImpl<>(userMapper.toOthersDTOs(users), pageable, communityUserPage.getTotalElements());
        } catch (Exception e) {
            LOGGER.error(CommunityService.class.getName() + " Exception Occurred");
            e.printStackTrace();
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public Page<Community> getCommunitiesFromLikeString(String communityString, Pageable pageable) {
        try {
            return communityRepository.findByCommunityNameContaining(communityString, pageable);
        } catch (Exception e) {
            LOGGER.error(CommunityService.class.getName() + " Exception Occurred");
            e.printStackTrace();
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public Page<UserDTO> searchUserInCommunityUsers(String communitySlug, String userString, Pageable pageable) {
        try {
            Page<CommunityUser> communityUserPage = communityUserRepository.findAllByUserContainingString(communityCommonService.getCommunity(communitySlug), userString, pageable);
            List<User> users = communityUserPage.getContent().stream()
                    .map(CommunityUser::getUser).collect(Collectors.toList());

            return new PageImpl<>(userMapper.toOthersDTOs(users), pageable, communityUserPage.getTotalElements());
        } catch (Exception e) {
            LOGGER.error(CommunityService.class.getName() + " Exception Occurred");
            e.printStackTrace();
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }
}