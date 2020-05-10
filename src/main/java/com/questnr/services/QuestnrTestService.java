package com.questnr.services;

import com.questnr.services.cloudinary.CloudinaryVideoService;
import com.questnr.services.user.UserCommonService;
import com.questnr.util.VideoCompression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class QuestnrTestService {

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    CloudinaryVideoService cloudinaryVideoService;

    @Autowired
    private AmazonS3Client amazonS3Client;

    public String uploadVideo(MultipartFile multipartFile) throws IOException {
        File source = amazonS3Client.convertMultiPartToFile(multipartFile);
        cloudinaryVideoService.uploadVideoFile(source, userCommonService.getS3BucketUserFolder() + "/xyz");
        return "Successful";
    }

    public String transformVideo() {
        return cloudinaryVideoService.getVideoTag(userCommonService.getS3BucketUserFolder() + "/xyz");
    }

    public String manipulateVideo(MultipartFile multipartFile) throws IOException, InterruptedException {
        File source = amazonS3Client.convertMultiPartToFile(multipartFile);
        File target = new File("xyz-out.mp4");
        Thread videoCompressionThread = new Thread(new VideoCompression(source, target), "VideoCompression-1");
        videoCompressionThread.run();
        videoCompressionThread.join();
        return "Successful";
    }
}
