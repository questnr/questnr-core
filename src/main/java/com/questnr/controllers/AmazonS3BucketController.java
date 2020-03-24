package com.questnr.controllers;

import com.questnr.responses.AvatarStorageData;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/v1/aws-storage")
public class AmazonS3BucketController {

    @Autowired
    UserCommonService userCommonService;

    private AmazonS3Client amazonS3Client;

    @Autowired
    AmazonS3BucketController(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @RequestMapping(value = "/upload-file", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public AvatarStorageData uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return this.amazonS3Client.uploadFile(file);
    }

    @RequestMapping(value = "/delete-file-using-url", method = RequestMethod.DELETE)
    public String deleteFileUsingFileUrl(@RequestPart(value = "url") String fileUrl) {
        return this.amazonS3Client.deleteFileFromS3BucketUsingFileUrl(fileUrl);
    }

    @RequestMapping(value = "/delete-file", method = RequestMethod.DELETE)
    public String deleteFileUsingPathToFile(@RequestPart(value = "url") String fileUrl) {
        return this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(fileUrl);
    }

    @RequestMapping(value = "/download-file", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> getFile(@RequestParam(value = "key") String key) throws IOException {
        byte[] data = this.amazonS3Client.getFile(key);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + key + "\"")
                .body(resource);

    }
}