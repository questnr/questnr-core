package com.questnr.services.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.SingletonManager;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class CloudinaryInitializer {

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void initialize() {
        try {
            Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret));
            SingletonManager manager = new SingletonManager();
            manager.setCloudinary(cloudinary);
            manager.init();
            LOGGER.info("Cloudinary application has been initialized");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
