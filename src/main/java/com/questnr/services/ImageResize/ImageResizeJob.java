package com.questnr.services.ImageResize;

import com.questnr.services.AmazonS3Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageResizeJob {

    @Autowired
    AmazonS3Client amazonS3Client;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public void createImageResizeJob(ImageResizeJobRequest imageResizeJobRequest) {
        ImageResizeProcessor.getInstance(this.amazonS3Client).add(imageResizeJobRequest);
    }
}
