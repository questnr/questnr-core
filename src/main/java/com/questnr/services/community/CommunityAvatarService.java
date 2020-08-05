package com.questnr.services.community;

import com.questnr.common.enums.ResourceType;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.AvatarDTO;
import com.questnr.model.entities.Avatar;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.AvatarMapper;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.responses.ResourceStorageData;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonService;
import com.questnr.services.ImageResize.ImageResizeJob;
import com.questnr.services.ImageResize.ImageResizeJobRequest;
import com.questnr.util.ImageCompression;
import com.questnr.util.ImageResizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class CommunityAvatarService {
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
    ImageResizeJob imageResizeJob;

    @Autowired
    AvatarMapper avatarMapper;

    public AvatarDTO uploadAvatar(long communityId, MultipartFile multipartFile) {
        Community community = communityCommonService.getCommunity(communityId);
        try {
                Avatar avatar = new Avatar();
                if(community.getAvatar() != null){
                    avatar.updateMetadata();
                    this.deleteAvatarFromS3(community);
                    avatar = community.getAvatar();
                }else{
                    avatar.addMetadata();
                }
                File file = commonService.convertMultiPartToFile(multipartFile);
                if (commonService.checkIfFileIsImage(file)) {
                    ResourceStorageData resourceStorageData;
                    String fileName = commonService.generateFileName(file);
                    try {
                        ImageResizeJobRequest imageResizeJobRequest = new ImageResizeJobRequest();
                        ImageResizer imageResizer = new ImageResizer();
                        imageResizer.setFileName(fileName);
                        if(commonService.getFileExtension(file).equals("png")) {
                            imageResizer.setInputFile(file);
                            imageResizeJobRequest.setFormat("png");
                            resourceStorageData = this.amazonS3Client.uploadFileToPath(file,
                                    communityCommonService.getAvatarPathToFile(communityId, fileName));
                        } else {
                            ImageCompression imageCompression = new ImageCompression();
                            imageCompression.setInputFile(file);
                            File compressedFile = imageCompression.doCompression();
                            if (file.exists()) file.delete();
                            imageResizer.setInputFile(compressedFile);
                            imageResizeJobRequest.setFormat(imageCompression.getFormat());
                            resourceStorageData = this.amazonS3Client.uploadFileToPath(compressedFile,
                                    communityCommonService.getAvatarPathToFile(communityId, fileName));
                        }

                        resourceStorageData.setResourceType(ResourceType.image);
                        imageResizeJobRequest.setImageResizer(imageResizer);
                        imageResizeJobRequest.setPathToDir(communityCommonService.getAvatarPathToDir(communityId));
                        imageResizeJob.createImageResizeJob(imageResizeJobRequest);
                        avatar.setAvatarKey(null);
                        avatar.setFileName(fileName);
                        avatar.setPathToDir(communityCommonService.getAvatarPathToDir(communityId));
                        community.setAvatar(avatar);
                        Community savedCommunity = communityRepository.save(community);
                        return avatarMapper.toAvatarDTO(savedCommunity.getAvatar());
                    } catch (Exception e) {
                        LOGGER.error(CommunityAvatarService.class.getName() + " Exception Occurred");
                        throw new InvalidRequestException("Error occurred. Please, try again!");
                    }
                } else {
                    throw new InvalidRequestException("Avatar can not be of video format");
                }
        } catch (IOException ex) {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public AvatarDTO getAvatar(String communitySlug) {
        return this.avatarMapper.toAvatarDTO(communityCommonService.getCommunity(communitySlug).getAvatar());
    }

    private void deleteAvatar(Community community) {
        if (!CommonService.isNull(community.getAvatar().getAvatarKey())) {
            try {
                this.deleteAvatarFromS3(community);
            } catch (Exception e) {
                throw new ResourceNotFoundException("Community avatar not found!");
            }
            try {
                community.setAvatar(null);
                communityRepository.save(community);
            } catch (Exception e) {
                LOGGER.error(User.class.getName() + " Exception Occurred");
                throw new InvalidRequestException("Error occurred. Please, try again!");
            }
        }
    }

    public void deleteAvatar(long communityId) {
        this.deleteAvatar(communityCommonService.getCommunity(communityId));
    }

    public void deleteAvatarFromS3(Community community){
        this.amazonS3Client.deleteAvatarFromS3(community.getAvatar());
    }

    public void deleteAvatarFromS3(Long communityId){
        this.deleteAvatarFromS3(communityCommonService.getCommunity(communityId));
    }

    public byte[] getAvatarInBytes(String communitySlug) {
        Community community = communityCommonService.getCommunity(communitySlug);
        if (!CommonService.isNull(community.getAvatar().getAvatarKey()) || !CommonService.isNull(community.getAvatar().getFileName())) {
            return this.amazonS3Client.getFile(community.getAvatar().getAvatarKey());
        }
        return null;
    }
}
