package com.questnr.services.community;

import com.questnr.common.enums.PublishStatus;
import com.questnr.exceptions.AlreadyExistsException;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.UserDTO;
import com.questnr.model.entities.*;
import com.questnr.model.mapper.UserMapper;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonService;
import com.questnr.services.CustomPageService;
import com.questnr.services.user.UserCommonService;
import com.questnr.util.SecureRandomService;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommunityService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    CommonService commonService;

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    CommunityAvatarService communityAvatarService;

    @Autowired
    SecureRandomService secureRandomService;

    @Autowired
    CustomPageService<User> customPageService;

    private String createCommunitySlug(Community community) {
        List<String> communityNameChunks = Arrays.asList(community.getCommunityName().toLowerCase().split("\\s"));
        String randomString = secureRandomService.getSecureRandom().toString();
        return String.join("-", communityNameChunks).replaceAll("[ ](?=[ ])|[^-_A-Za-z0-9 ]+", "") +
                "-" +
                randomString;
    }

    private String getCommunityTags(Community community) {
        List<String> descChunks = Arrays.asList(community.getDescription().toLowerCase().split("\\s"));
        return String.join(" ", descChunks.subList(0, descChunks.size())).replaceAll("[ ](?=[ ])|[^A-Za-z0-9 ]+", "");
    }

    public Community getCommunityByCommunityName(String communityName) {
        Community community = communityRepository.findByCommunityName(communityName);
        if (community != null) {
            return community;
        }
        throw new ResourceNotFoundException("Community not found!");
    }

    private CommunityMetaInformation getCommunityDescMetaInformation(Community community) {
        MetaInformation metaInfo = new MetaInformation();
        metaInfo.setAttributeType("name");
        metaInfo.setType("description");
        metaInfo.setContent(community.getDescription());
        CommunityMetaInformation communityMeta = new CommunityMetaInformation();
        communityMeta.setMetaInformation(metaInfo);
        return communityMeta;
    }

    public Community setCommunityMetaInformation(Community community) {
        if (community != null) {
            List<CommunityMetaInformation> metaList = new LinkedList<>();
            if (community.getMetaList() == null || community.getMetaList().size() == 0) {
                metaList.add(this.getCommunityDescMetaInformation(community));
            } else {
                boolean foundDesc = false;
                for (CommunityMetaInformation meta : community.getMetaList()) {
                    if (meta != null && meta.getMetaInformation() != null) {
                        if (meta.getMetaInformation().getType().equals("description")) {
                            foundDesc = true;
                            break;
                        }
                    }
                }
                if (!foundDesc) {
                    metaList.add(this.getCommunityDescMetaInformation(community));
                }
            }
            community.getMetaList().addAll(metaList);
        }
        return community;
    }

    public boolean checkCommunityNameExists(String communityName) throws AlreadyExistsException{
        if(communityRepository.countByCommunityName(communityName) == 0){
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

    public Page<User> getUsersOfCommunity(String communitySlug, Pageable pageable) {
        try {
            List<CommunityUser> communityUserList = new ArrayList<>(communityCommonService.getCommunity(communitySlug).getUsers());

            Comparator<CommunityUser> communityUserComparator = Comparator.comparing(CommunityUser::getCreatedAt);
            communityUserList.sort(communityUserComparator.reversed());

            List<User> users = communityUserList.stream()
                    .map(CommunityUser::getUser).collect(Collectors.toList());

            return customPageService.customPage(users, pageable);
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

    public Page<User> searchUserInCommunityUsers(String communitySlug, String userString, Pageable pageable) {
        Community community = communityCommonService.getCommunity(communitySlug);
        List<CommunityUser> communityUsers = community.getUsers().stream().filter(communityUser ->
                communityUser.getUser().getUsername().toLowerCase().matches("(.*)" + userString.toLowerCase() + "(.*)")
        ).collect(Collectors.toList());
        List<User> users = communityUsers.stream().map(CommunityUser::getUser).collect(Collectors.toList());
        return customPageService.customPage(users, pageable);
    }
}