package com.questnr.services;

import com.amazonaws.services.s3.model.Tag;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AmazonS3Service {
    public List<Tag> getPublicObjectTags() {
        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag("access", "public"));
        return tagList;
    }

    public boolean areTagsEqual(Tag firstTag, Tag secondTag){
        return firstTag.equals(secondTag);
    }
}