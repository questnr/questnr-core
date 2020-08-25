package com.questnr.model.mapper;

import com.questnr.common.enums.PostActionPrivacy;
import com.questnr.common.enums.ResourceType;
import com.questnr.model.dto.MediaDTO;
import com.questnr.model.entities.User;
import com.questnr.model.entities.media.Media;
import com.questnr.model.entities.media.PostMedia;
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
public class MediaMapper {

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

    public MediaDTO toPostMediaDTO(Media media) {
        User user = null;
        try {
            user = userCommonService.getUser();
        } catch (Exception e) {

        }
        MediaDTO mediaDTO = new MediaDTO();
        mediaDTO.setResourceType(media.getResourceType());
        mediaDTO.setFileExtension(media.getFileExtension());
        // If user is not logged in and media is application, then it can not be downloaded
        if (!(user == null && media.getResourceType() == ResourceType.application)) {
            mediaDTO.setPostMediaLink(this.amazonS3Client.getS3BucketUrl(media.getMediaKey(), PostActionPrivacy.public_post));
        }
        return mediaDTO;
    }

    public List<MediaDTO> toPostMediaDTOList(List<PostMedia> postMediaList) {
        return postMediaList.stream().map(this::toPostMediaDTO).collect(Collectors.toList());
    }
}
