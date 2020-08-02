package com.questnr.services;

import com.questnr.common.PostMediaHandlingEntity;
import com.questnr.common.enums.ResourceType;
import com.questnr.model.entities.media.CommentMedia;
import com.questnr.model.entities.media.Media;
import com.questnr.model.entities.media.PostMedia;
import com.questnr.responses.ResourceStorageData;
import com.questnr.services.user.UserCommonService;
import com.questnr.util.ImageCompression;
import com.questnr.util.VideoCompression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PostMediaService {

    @Autowired
    CommonService commonService;

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    UserCommonService userCommonService;

    public ResourceStorageData handleFile(File file, PostMediaHandlingEntity postMediaHandlingEntity){
        ResourceStorageData resourceStorageData;
        if (postMediaHandlingEntity.isBelongCommunity())

            resourceStorageData = this.amazonS3Client.uploadFile(file,
                    postMediaHandlingEntity.getEntityId());
        else if(postMediaHandlingEntity.isBelongComment())
            resourceStorageData = this.amazonS3Client.uploadFileToPath(file,
                    userCommonService.getCommentPathToFile(commonService.generateFileName(file)));
        else
            resourceStorageData = this.amazonS3Client.uploadFile(file);
        return resourceStorageData;
    }

    public List<Media> handleFiles(List<MultipartFile> files, PostMediaHandlingEntity postMediaHandlingEntity) {
        List<Media> postMediaList;
        postMediaList = files.stream().map(multipartFile -> {
            ResourceStorageData resourceStorageData = new ResourceStorageData();
            try {
                File file = commonService.convertMultiPartToFile(multipartFile);
                if (commonService.checkIfFileIsImage(file)) {
                    try {
                        if (commonService.getFileExtension(file).equals("png")) {
                            resourceStorageData = this.handleFile(file, postMediaHandlingEntity);
                        } else {
                            ImageCompression imageCompression = new ImageCompression();
                            imageCompression.setInputFile(file);
                            File compressedFile = imageCompression.doCompression();
                            if (file.exists()) file.delete();
                            resourceStorageData = this.handleFile(compressedFile, postMediaHandlingEntity);
                        }
                        resourceStorageData.setResourceType(ResourceType.image);
                    } catch (Exception e) {

                    }
                } else if(!postMediaHandlingEntity.isBelongComment()
                && commonService.checkIfFileIsVideo(file)){
                    /*
                    Comment can not have video files
                     */

                    String fileName = "out_" + commonService.generateFileName(file);
                    File target = new File(fileName);
                    try {
                        VideoCompression videoCompression = new VideoCompression(file, target);
                        Thread videoCompressionThread = new Thread(videoCompression, fileName);
                        videoCompressionThread.start();
                        videoCompressionThread.join();
                        resourceStorageData = this.handleFile(target, postMediaHandlingEntity);
                        resourceStorageData.setResourceType(ResourceType.video);
                        if (file.exists()) file.delete();
                    } catch (InterruptedException e) {

                    }
                } else if(commonService.checkIfFileIsApplication(file)){
                    resourceStorageData = this.handleFile(file, postMediaHandlingEntity);
                    resourceStorageData.setResourceType(ResourceType.application);
                }
                if (resourceStorageData.getKey() != null && !CommonService.isNull(resourceStorageData.getKey())) {
                    Media media;
                    if(postMediaHandlingEntity.isBelongComment()){
                        media = new CommentMedia();
                    }else {
                        media = new PostMedia();
                    }
                    media.setMediaKey(resourceStorageData.getKey());
                    media.setResourceType(resourceStorageData.getResourceType());
                    return media;
                }
            } catch (IOException ex) {

            }
            return null;
        }).collect(Collectors.toList());
        return postMediaList.stream().filter(Objects::nonNull
        ).collect(Collectors.toList());
    }
}
