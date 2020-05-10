package com.questnr.services.cloudinary;


import com.cloudinary.Singleton;
import com.cloudinary.Transformation;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static com.cloudinary.utils.ObjectUtils.asMap;

@Service
public class CloudinaryVideoService {

    public String getVideoTag(String pathToFile) {
        return Singleton.getCloudinary().url().signed(true).videoTag(pathToFile, asMap(
                "controls", true,
                "loop", true));
    }

    public void uploadVideoFile(File source, String pathToFile) throws IOException {
        if (source == null) return;
        Map result =Singleton.getCloudinary().uploader().upload(source,
                asMap("resource_type", "video",
                        "type", "private",
                        "eager", Arrays.asList(
                                new Transformation().quality(50).overlay("questnr_icon").opacity(50).width(60).gravity("south_east").y(15).x(60)),
                        "eager_async", true));

    }
}
