package com.questnr.model.mapper;

import com.questnr.model.dto.PostMediaDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostMedia;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonService;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMediaMapper {

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    CommonService commonService;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    PostActionRepository postActionRepository;

//    public String getPostMediaLink(PostMedia postMedia){
//        PostAction postActon = postActionRepository.findByPostMediaListContaining(postMedia.getPostMediaId());
//        if (postActon.getCommunity() != null && commonService.isNull(postActon.getCommunity().getCommunityId().toString()))
//           return this.amazonS3Client.getS3BucketUrl(communityCommonService.joinPathToFile(postMedia.getMediaKey(), postActon.getCommunity().getCommunityId()));
//        else
//            return this.amazonS3Client.getS3BucketUrl(userCommonService.joinPathToFile(postMedia.getMediaKey()));
//    }

    public PostMediaDTO toPostMediaDTO(PostMedia postMedia) {
        PostMediaDTO postMediaDTO = new PostMediaDTO();
        postMediaDTO.setPostMediaId(postMedia.getPostMediaId());
        postMediaDTO.setPostMediaLink(this.amazonS3Client.getS3BucketUrl(postMedia.getMediaKey()));
        return postMediaDTO;
    }

    public List<PostMediaDTO> toPostMediaDTOList(List<PostMedia> postMediaList) {
        return postMediaList.stream().map(postMedia -> {
            PostMediaDTO postMediaDTO = new PostMediaDTO();
            postMediaDTO.setPostMediaId(postMedia.getPostMediaId());
            postMediaDTO.setPostMediaLink(this.amazonS3Client.getS3BucketUrl(postMedia.getMediaKey()));
            return postMediaDTO;
        }).collect(Collectors.toList());
    }
}
