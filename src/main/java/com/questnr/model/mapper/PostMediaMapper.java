package com.questnr.model.mapper;

import com.questnr.model.dto.PostMediaDTO;
import com.questnr.model.entities.PostMedia;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMediaMapper {

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    CommonUserService commonUserService;

    public PostMediaDTO toPostMediaDTO(PostMedia postMedia){
        PostMediaDTO postMediaDTO = new PostMediaDTO();
        postMediaDTO.setPostMediaId(postMedia.getPostMediaId());
        postMediaDTO.setPostMediaLink(this.amazonS3Client.getS3BucketUrl(commonUserService.joinPathToFile(postMedia.getMediaKey())));
        return postMediaDTO;
    }

    public List<PostMediaDTO> toPostMediaDTOList(List<PostMedia> postMediaList){
        return postMediaList.stream().map(postMedia -> {
            PostMediaDTO postMediaDTO = new PostMediaDTO();
            postMediaDTO.setPostMediaId(postMedia.getPostMediaId());
            postMediaDTO.setPostMediaLink(this.amazonS3Client.getS3BucketUrl(commonUserService.joinPathToFile(postMedia.getMediaKey())));
            return postMediaDTO;
        }).collect(Collectors.toList());
    }
}
