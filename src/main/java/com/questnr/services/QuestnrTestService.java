package com.questnr.services;

import com.questnr.responses.ResourceStorageData;
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
    private CommonService commonService;

    public ResourceStorageData uploadVideo(MultipartFile multipartFile) throws IOException {
        File source = commonService.convertMultiPartToFile(multipartFile);
        return cloudinaryVideoService.uploadFile(source, "video");
    }

    public String transformVideo(String fileName, String format) throws Exception {
        return cloudinaryVideoService.getPreSignedURL(fileName, format);
    }

    public String manipulateVideo(MultipartFile multipartFile) throws IOException, InterruptedException {
        File source = commonService.convertMultiPartToFile(multipartFile);
        File target = new File("xyz-out.mp4");
        Thread videoCompressionThread = new Thread(new VideoCompression(source, target), "VideoCompression-1");
        videoCompressionThread.run();
        videoCompressionThread.join();
        return "Successful";
    }
}
