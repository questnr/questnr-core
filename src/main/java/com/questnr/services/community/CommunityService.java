package com.questnr.services.community;

import com.questnr.common.enums.CommunityPrivacy;
import com.questnr.exceptions.AlreadyExistsException;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.community.CommunityCardDTO;
import com.questnr.model.dto.user.UserOtherDTO;
import com.questnr.model.entities.*;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.model.mapper.UserMapper;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.model.repositories.CommunityTagRepository;
import com.questnr.model.repositories.CommunityUserRepository;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.specifications.CommunityEntityTagSpecifications;
import com.questnr.requests.CommunityRequest;
import com.questnr.requests.CommunityUpdateRequest;
import com.questnr.requests.UserInterestsRequest;
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

    @Autowired
    private PostActionRepository postActionRepository;

    @Autowired
    private CommunityTagService communityTagService;

    @Autowired
    private CommunityTagRepository communityTagRepository;

    @Autowired
    private CommunityMapper communityMapper;

    public CommunityService() {
        this.userMapper = Mappers.getMapper(UserMapper.class);
        this.communityMapper = Mappers.getMapper(CommunityMapper.class);
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

    public Community createCommunity(Community community,
                                     MultipartFile multipartFile,
                                     CommunityRequest communityRequest) {
        Community communitySaved = this.createCommunity(community, communityRequest);
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

    public Community updateCommunity(Community community, CommunityUpdateRequest communityUpdateRequest) {
        try {
            community.setDescription(communityUpdateRequest.getDescription());
            return communityRepository.save(community);
        } catch (Exception e) {
            LOGGER.error(CommunityService.class.getName() + " Exception Occurred");
        }
        return null;
    }

    public Community createCommunity(Community community, CommunityRequest communityRequest) {
        if (community != null) {
            if (this.checkCommunityNameExists(community.getCommunityName())) {
                try {
                    assert communityRequest.getCommunityTags() != null;
                    List<String> tagList = Arrays.asList(
                            communityTagService.getCommunityTags(communityRequest.getCommunityTags()));
                    if (tagList.size() >= 5
                            || tagList.size() < 2) {
                        throw new InvalidRequestException("Community Tags are not valid");
                    }
                    List<CommunityTag> communityTagList = communityTagService.parseAndStoreCommunityTags(tagList, community);
                    community.setTags(communityTagList);
                    community.setCommunityPrivacy(CommunityPrivacy.pub);
                    community.setOwnerUser(userCommonService.getUser());
                    community.addMetadata();
                    community.setSlug(this.createCommunitySlug(community));
                    Community savedCommunity = communityRepository.saveAndFlush(community);

                    return savedCommunity;
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
            if (!CommonService.isNull(community.getAvatar().getAvatarKey())) {
                try {
                    this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(community.getAvatar().getAvatarKey());
                } catch (Exception e) {
                    LOGGER.error(CommunityService.class.getName() + " Exception Occurred. Couldn't able to delete resources of community on the cloud.");
                }
            }
            List<PostAction> postActionList = postActionRepository.findByCommunity(community);
            for (PostAction postAction : postActionList) {
                postActionRepository.delete(postAction);
            }
            communityRepository.delete(community);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Community not Found!");
        }
    }

    public List<Community> findAllCommunityNames() {
        try {
            return communityRepository.findAll();
        } catch (Exception e) {
            LOGGER.error(CommunityService.class.getName() + " Exception Occurred");
            e.printStackTrace();
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public Page<UserOtherDTO> getUsersOfCommunity(String communitySlug, Pageable pageable) {
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
            communityString = communityString.toLowerCase();
            return communityRepository.findByCommunityNameContaining(communityString, pageable);
        } catch (Exception e) {
            LOGGER.error(CommunityService.class.getName() + " Exception Occurred");
            e.printStackTrace();
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public Page<UserOtherDTO> searchUserInCommunityUsers(String communitySlug, String userString, Pageable pageable) {
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

    public Page<CommunityCardDTO> getCommunitySuggestionsForGuide(UserInterestsRequest userInterestsRequest,
                                                                  Pageable pageable){
        List<String> tagList = Arrays.asList(this.communityTagService.getCommunityTags(userInterestsRequest.getUserInterests()));
        tagList = this.communityTagService.parseCommunityTags(tagList);
        Page<CommunityTag> communityTagPage = communityTagRepository.findAll(
                CommunityEntityTagSpecifications.findEntityTagInList(tagList)
        , pageable);
        List<Community> communityList = communityTagPage.stream().filter(entityTag ->
                entityTag.getCommunity()!=null).map(CommunityTag::getCommunity).collect(Collectors.toList());
        return new PageImpl<>(this.communityMapper.toCommunityCards(communityList), pageable, communityTagPage.getTotalElements());
    }
}