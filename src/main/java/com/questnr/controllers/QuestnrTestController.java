package com.questnr.controllers;

import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonService;
import com.questnr.services.QuestnrTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/admin")
public class QuestnrTestController {

    @Autowired
    private QuestnrTestService questnrTestService;

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Autowired
    private CommonService commonService;

//    @RequestMapping(value = "/video-compression/manipuate/{quality}", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
//    public String manipulateVideo(@RequestPart(value = "file") MultipartFile file, @PathVariable int quality) throws IOException, InterruptedException {
//        return questnrTestService.manipulateVideo(file, quality);
//    }
//
//    @RequestMapping(value = "/upload/private", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
//    public void uploadFilePrivate(@RequestPart(value = "file") MultipartFile file) throws Exception{
//        this.amazonS3Client.uploadFile(commonService.convertMultiPartToFile(file), PostActionPrivacy.private_post);
//    }
//
//    @RequestMapping(value = "/upload/public", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
//    public void uploadFilePublic(@RequestPart(value = "file") MultipartFile file) throws Exception{
//        this.amazonS3Client.uploadFile(commonService.convertMultiPartToFile(file), PostActionPrivacy.public_post);
//    }

//    // Set blog title if user has blog title as null
//    @RequestMapping(value = "/blog/title", method = RequestMethod.POST)
//    public Map<String, Integer> makeBlogTitle(){
//        return questnrTestService.makeBlogTitle();
//    }

    // Set blog title if user has blog title as null
    @RequestMapping(value = "/copy/objects", method = RequestMethod.POST)
    public Map<String, String> copyAvatars(){
        return questnrTestService.copyAvatars();
    }
}
