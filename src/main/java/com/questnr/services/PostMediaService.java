package com.questnr.services;

import com.questnr.common.PostMediaHandlingEntity;
import com.questnr.common.enums.ResourceType;
import com.questnr.model.entities.PostMedia;
import com.questnr.responses.ResourceStorageData;
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

    public List<PostMedia> handleFiles(List<MultipartFile> files, PostMediaHandlingEntity postMediaHandlingEntity) {
        List<PostMedia> postMediaList;
        postMediaList = files.stream().map(multipartFile -> {
            ResourceStorageData resourceStorageData = new ResourceStorageData();
            try {
                File file = commonService.convertMultiPartToFile(multipartFile);
                if (commonService.checkIfFileIsImage(file)) {
                    try {
                        if (commonService.getFileExtension(file).equals("png")) {
                            if (postMediaHandlingEntity.isBelongCommunity())
                                resourceStorageData = this.amazonS3Client.uploadFile(file,
                                        postMediaHandlingEntity.getEntityId());
                            else
                                resourceStorageData = this.amazonS3Client.uploadFile(file);
                        } else {
                            ImageCompression imageCompression = new ImageCompression();
                            imageCompression.setInputFile(file);
                            File compressedFile = imageCompression.doCompression();
                            if (file.exists()) file.delete();
                            if (postMediaHandlingEntity.isBelongCommunity())
                                resourceStorageData = this.amazonS3Client.uploadFile(compressedFile,
                                        postMediaHandlingEntity.getEntityId());
                            else
                                resourceStorageData = this.amazonS3Client.uploadFile(compressedFile);
                        }
                        resourceStorageData.setResourceType(ResourceType.image);
                    } catch (Exception e) {

                    }
                } else {
                    String fileName = "out_" + commonService.generateFileName(file);
                    File target = new File(fileName);
                    try {
                        VideoCompression videoCompression = new VideoCompression(file, target);
                        Thread videoCompressionThread = new Thread(videoCompression, fileName);
                        videoCompressionThread.start();
                        videoCompressionThread.join();
                        if (postMediaHandlingEntity.isBelongCommunity())
                            resourceStorageData = this.amazonS3Client.uploadFile(target,
                                    postMediaHandlingEntity.getEntityId());
                        else
                            resourceStorageData = this.amazonS3Client.uploadFile(target);
                        resourceStorageData.setResourceType(ResourceType.video);
                        if (file.exists()) file.delete();
                    } catch (InterruptedException e) {

                    }
                }
                if (resourceStorageData.getKey() != null && !CommonService.isNull(resourceStorageData.getKey())) {
                    PostMedia postMedia = new PostMedia();
                    postMedia.setMediaKey(resourceStorageData.getKey());
                    postMedia.setResourceType(resourceStorageData.getResourceType());
                    return postMedia;
                }
            } catch (IOException ex) {

            }
            return null;
        }).collect(Collectors.toList());
        return postMediaList.stream().filter(Objects::nonNull
        ).collect(Collectors.toList());
    }
}
