package com.questnr.services.cloudinary;


import com.cloudinary.Singleton;
import com.cloudinary.Transformation;
import com.questnr.responses.ResourceStorageData;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static com.cloudinary.utils.ObjectUtils.asMap;

@Service
public class CloudinaryVideoService {

    public String getPreSignedURL(String fileName, String format) throws Exception {
//        return Singleton.getCloudinary().url().signed(true).secure(true).videoTag(pathToFile, asMap(
//                "controls", true,
//                "loop", true));
        Date expiration = new Date();
        Long expTimeMillis = expiration.getTime() / 1000;
        expTimeMillis += 40;
        return Singleton.getCloudinary().privateDownload(fileName, format,
                asMap("resource_type", "video",
                        "expires_at", expTimeMillis.toString()));
    }

    public ResourceStorageData uploadFile(File source, String resourceType) throws IOException {
        if (source == null) return new ResourceStorageData();
        Map result = Singleton.getCloudinary().uploader().upload(source,
                asMap("resource_type", resourceType,
                        "type", "private",
                        "eager", Arrays.asList(
                                new Transformation().quality(50).overlay("questnr_icon").opacity(50).width(60).gravity("south_east").y(15).x(60)),
                        "eager_async", true));


        ResourceStorageData resourceStorageData = new ResourceStorageData();
        return resourceStorageData.map(result);
    }
}
