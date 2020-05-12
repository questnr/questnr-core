package com.questnr.services;

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
    private CommonService commonService;

    public String manipulateVideo(MultipartFile multipartFile, int quality) throws IOException, InterruptedException {
        File source = commonService.convertMultiPartToFile(multipartFile);
        File target = new File("out_"+source.getName());
        VideoCompression videoCompression = new VideoCompression(source,target);
        videoCompression.setQuality(quality);
        Thread videoCompressionThread = new Thread(videoCompression, "VideoCompression-1");
        videoCompressionThread.start();
        videoCompressionThread.join();
        return "Successful";
    }
}
