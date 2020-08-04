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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class UserAvatarService {
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

    @Value("${amazonProperties.publicAssetPath}")
    private String publicAssetPath;

    public AvatarDTO uploadAvatar(MultipartFile multipartFile) {
        User user = userCommonService.getUser();
        try {
            Avatar avatar = new Avatar();
            if (user.getAvatar() != null) {
                avatar.updateMetadata();
                this.deleteAvatarFromS3(user);
                avatar = user.getAvatar();
            } else {
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
                                userCommonService.getAvatarPathToFile(fileName));
                        resourceStorageData.setResourceType(ResourceType.image);
                    } else {
                        ImageCompression imageCompression = new ImageCompression();
                        imageCompression.setInputFile(file);
                        File compressedFile = imageCompression.doCompression();
                        if (file.exists()) file.delete();
                        imageResizer.setInputFile(compressedFile);
                        imageResizeJobRequest.setFormat(imageCompression.getFormat());
                        resourceStorageData = this.amazonS3Client.uploadFileToPath(compressedFile,
                                userCommonService.getAvatarPathToFile(fileName));
                        resourceStorageData.setResourceType(ResourceType.image);
                    }

                    imageResizeJobRequest.setImageResizer(imageResizer);
                    imageResizeJobRequest.setPathToDir(Paths.get(
                            publicAssetPath,
                            userCommonService.getAvatarPathToDir()).toString());
                    imageResizeJob.createImageResizeJob(imageResizeJobRequest);

                    avatar.setAvatarKey(null);
                    avatar.setFileName(fileName);
                    avatar.setPathToDir(userCommonService.getAvatarPathToDir());
                    user.setAvatar(avatar);
                    User savedUser = userRepository.save(user);
                    return avatarMapper.toAvatarDTO(savedUser.getAvatar());
                } catch (Exception e) {
                    LOGGER.error(UserAvatarService.class.getName() + " Exception Occurred");
                    throw new InvalidRequestException("Error occurred. Please, try again!");
                }
            } else {
                throw new InvalidRequestException("Avatar can not be of video format");
            }
        } catch (IOException ex) {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public AvatarDTO getUserAvatar(User user) {
        return avatarMapper.toAvatarDTO(user.getAvatar());
    }

    public AvatarDTO getUserAvatar() {
        return avatarMapper.toAvatarDTO(userCommonService.getUser().getAvatar());
    }

    public AvatarDTO getUserAvatar(String userSlug) {
        return avatarMapper.toAvatarDTO(userCommonService.getUser(userSlug).getAvatar());
    }

    private void deleteAvatar(User user) {
        if (!CommonService.isNull(user.getAvatar().getAvatarKey())) {
            try {
                this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(user.getAvatar().getAvatarKey());
            } catch (Exception e) {
                throw new ResourceNotFoundException("User avatar not found!");
            }
            try {
                user.setAvatar(null);
                userRepository.save(user);
            } catch (Exception e) {
                LOGGER.error(UserAvatarService.class.getName() + " Exception Occurred");
                throw new InvalidRequestException("Error occurred. Please, try again!");
            }
        }
    }

    public void deleteAvatar() {
        this.deleteAvatar(userCommonService.getUser());
    }

    public void deleteAvatarFromS3(User user) {
        this.amazonS3Client.deleteAvatarFromS3(user.getAvatar());
    }

    public void deleteAvatarFromS3() {
        this.deleteAvatarFromS3(userCommonService.getUser());
    }

    public byte[] getAvatar() {
        User user = userCommonService.getUser();
        if (!commonService.isNull(user.getAvatar().getAvatarKey())) {
            return this.amazonS3Client.getFile(user.getAvatar().getAvatarKey());
        }
        return null;
    }
}
