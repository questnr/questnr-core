package com.questnr.services.user;

import com.questnr.common.enums.ResourceType;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.AvatarDTO;
import com.questnr.model.entities.Avatar;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.AvatarMapper;
import com.questnr.model.repositories.UserRepository;
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
public class UserBannerService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    CommonService commonService;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageResizeJob imageResizeJob;

    @Autowired
    AvatarMapper avatarMapper;

    public AvatarDTO uploadBanner(MultipartFile multipartFile) {
        User user = userCommonService.getUser();
        try {
            Avatar avatar = new Avatar();
            if(user.getBanner() != null){
                avatar.updateMetadata();
                this.deleteBannerFromS3(user);
                avatar = user.getBanner();
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
                    if (commonService.getFileExtension(file).equals("png")) {
                        imageResizer.setInputFile(file);
                        imageResizeJobRequest.setFormat("png");
                        resourceStorageData = this.amazonS3Client.uploadFileToPath(file,
                                userCommonService.getBannerPathToFile(fileName));
                        resourceStorageData.setResourceType(ResourceType.image);
                    } else {
                        ImageCompression imageCompression = new ImageCompression();
                        imageCompression.setInputFile(file);
                        File compressedFile = imageCompression.doCompression();
                        if(file.exists()) file.delete();
                        imageResizer.setInputFile(compressedFile);
                        imageResizeJobRequest.setFormat(imageCompression.getFormat());
                        resourceStorageData = this.amazonS3Client.uploadFileToPath(compressedFile,
                                userCommonService.getBannerPathToFile(fileName));
                        resourceStorageData.setResourceType(ResourceType.image);
                    }

                    imageResizeJobRequest.setImageResizer(imageResizer);
                    imageResizeJobRequest.setPathToDir(userCommonService.getBannerPathToDir());
                    imageResizeJob.createImageResizeJob(imageResizeJobRequest);

                    avatar.setAvatarKey(null);
                    avatar.setFileName(fileName);
                    avatar.setPathToDir(userCommonService.getBannerPathToDir());
                    user.setBanner(avatar);
                    User savedUser = userRepository.save(user);
                   return avatarMapper.toAvatarDTO(savedUser.getBanner());
                } catch (Exception e) {
                    LOGGER.error(UserBannerService.class.getName() + " Exception Occurred");
                    throw new InvalidRequestException("Error occurred. Please, try again!");
                }
            } else {
                throw new InvalidRequestException("Banner can not be of video format");
            }
        } catch (IOException ex) {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public AvatarDTO getUserBanner(User user) {
        return avatarMapper.toAvatarDTO(user.getBanner());
    }

    public AvatarDTO getUserBanner() {
        return avatarMapper.toAvatarDTO(userCommonService.getUser().getBanner());
    }

    public AvatarDTO getUserBanner(String userSlug) {
        return avatarMapper.toAvatarDTO(userCommonService.getUser(userSlug).getBanner());
    }

    private void deleteBanner(User user) {
        if (!CommonService.isNull(user.getBanner().getAvatarKey())) {
            try {
                this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(user.getBanner().getAvatarKey());
            } catch (Exception e) {
                throw new ResourceNotFoundException("User avatar not found!");
            }
            try {
                user.setAvatar(null);
                userRepository.save(user);
            } catch (Exception e) {
                LOGGER.error(UserBannerService.class.getName() + " Exception Occurred");
                throw new InvalidRequestException("Error occurred. Please, try again!");
            }
        }
    }

    public void deleteBanner() {
       this.deleteBanner(userCommonService.getUser());
    }

    public void deleteBannerFromS3(User user){
        this.amazonS3Client.deleteAvatarFromS3(user.getBanner());
    }

    public void deleteBannerFromS3(){
        this.deleteBannerFromS3(userCommonService.getUser());
    }
}
