package com.questnr.controllers;

import com.questnr.services.QuestnrTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/v1/admin")
public class QuestnrTestController {

    @Autowired
    private QuestnrTestService questnrTestService;

    @RequestMapping(value = "/video-compression/upload", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String uploadVideo(@RequestPart(value = "file") MultipartFile file) throws IOException {
        return questnrTestService.uploadVideo(file);
    }

    @RequestMapping(value = "/video-compression/transform", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String transformVideo() {
        return questnrTestService.transformVideo();
    }

    @RequestMapping(value = "/video-compression/manipuate", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String manipulateVideo(@RequestPart(value = "file") MultipartFile file) throws IOException, InterruptedException {
        return questnrTestService.manipulateVideo(file);
    }
}
