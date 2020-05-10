package com.questnr.services.community;

import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.PostActionForCommunityDTO;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostMedia;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.responses.ResourceStorageData;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonService;
import com.questnr.services.PostActionService;
import com.questnr.services.user.UserCommonService;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunityPostActionService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    PostActionService postActionService;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    final PostActionMapper postActionMapper;

    @Autowired
    CommonService commonService;

    CommunityPostActionService() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    public Page<PostAction> getAllPostActionsByCommunityId(long communityId, Pageable pageable) {
        Community community = communityCommonService.getCommunity(communityId);
        if (community != null)
            return postActionRepository.findAllByCommunityOrderByCreatedAtDesc(community, pageable);
        throw new ResourceNotFoundException("Community not found!");
    }

    public PostActionForCommunityDTO creatPostAction(PostAction postAction, List<MultipartFile> files, long communityId) {
        if (postAction != null) {
            List<PostMedia> postMediaList;
            postMediaList = files.stream().map(multipartFile -> {
                try{
                    File file = commonService.convertMultiPartToFile(multipartFile);
                    if (commonService.checkIfFileIsImage(file)) {
                        ResourceStorageData resourceStorageData = this.amazonS3Client.uploadFile(file, communityId);
                        PostMedia postMedia = new PostMedia();
                        postMedia.setMediaKey(resourceStorageData.getKey());
                        postMedia.setResourceType(resourceStorageData.getResourceType());
                        return postMedia;
                    } else {

                    }
                }catch (IOException ex){

                }
                return null;
            }).collect(Collectors.toList());
            postAction.setCommunity(communityCommonService.getCommunity(communityId));
            postAction.setPostMediaList(postMediaList);
            return postActionMapper.toPostActionForCommunityDTO(postActionService.creatPostAction(postAction));
        } else {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public PostActionForCommunityDTO creatPostAction(PostAction postAction, long communityId) {
        if (postAction != null) {
            if (postAction.getText().length() == 0) {
                throw new InvalidRequestException("Text can not be empty!");
            }
            postAction.setCommunity(communityCommonService.getCommunity(communityId));
            return postActionMapper.toPostActionForCommunityDTO(postActionService.creatPostAction(postAction));
        } else {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }
}
