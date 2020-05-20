package com.questnr.services.community;

import com.questnr.common.enums.PublishStatus;
import com.questnr.exceptions.AlreadyExistsException;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.CommunityDTO;
import com.questnr.model.entities.*;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonService;
import com.questnr.services.CustomPageService;
import com.questnr.services.SharableLinkService;
import com.questnr.services.user.UserCommonService;
import com.questnr.util.SecureRandomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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

    @Value("${questnr.domain}")
    String QUEST_NR_DOMAIN;

    @Autowired
    SharableLinkService sharableLinkService;

    @Value("${facebook.appid}")
    private String fbAppId;

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

    private CommunityMetaInformation getCommunityMetaInformation(String attrType, String type, String content){
        MetaInformation metaInfo = new MetaInformation();
        metaInfo.setAttributeType(attrType);
        metaInfo.setType(type);
        metaInfo.setContent(content);
        CommunityMetaInformation communityMetaInformation = new CommunityMetaInformation();
        communityMetaInformation.setMetaInformation(metaInfo);
        return communityMetaInformation;
    }

    private List<CommunityMetaInformation> getCommunityMetaInformationList(CommunityDTO communityDTO) {
        List<CommunityMetaInformation> communityMetaInformationList = new ArrayList<>();

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "name",
                "description",
                CommonService.removeSpecialCharacters(communityDTO.getDescription())
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "name",
                "author",
                communityDTO.getOwnerUserDTO().getFirstName()+ " "+ communityDTO.getOwnerUserDTO().getFirstName()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "name",
                "robots",
                "index, follow, max-image-preview:standard"
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "name",
                "googlebot",
                "index, follow, max-image-preview:standard"
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "og:url",
                sharableLinkService.getCommunitySharableLink(communityDTO.getSlug()).getClickAction()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "og:title",
                communityDTO.getCommunityName()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "og:image",
                communityDTO.getAvatarDTO().getAvatarLink()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "og:type",
                "website"
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "fb:app_id",
                fbAppId
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "twitter:title",
                communityDTO.getCommunityName()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "twitter:description",
                communityDTO.getDescription()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "twitter:url",
                sharableLinkService.getCommunitySharableLink(communityDTO.getSlug()).getClickAction()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "twitter:image",
                communityDTO.getAvatarDTO().getAvatarLink()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "twitter:image",
                communityDTO.getAvatarDTO().getAvatarLink()
        ));

        return communityMetaInformationList;
    }

    public CommunityDTO setCommunityMetaInformation(CommunityDTO communityDTO) {
        if (communityDTO != null) {
            communityDTO.getMetaList().addAll(this.getCommunityMetaInformationList(communityDTO));
        }
        return communityDTO;
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