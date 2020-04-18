package com.questnr.services.community;

import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostMedia;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.responses.AvatarStorageData;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.PostActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    public Page<PostAction> getAllPostActionsByCommunityId(long communityId, Pageable pageable) {
        Community community = communityCommonService.getCommunity(communityId);
        if (community != null)
            return postActionRepository.findAllByCommunityOrderByCreatedAtDesc(community, pageable);
        throw new ResourceNotFoundException("Community not found!");
    }

    public PostAction creatPostAction(PostAction postAction, List<MultipartFile> files, long communityId) {
        if (postAction != null) {
            List<PostMedia> postMediaList;
            postMediaList = files.stream().map(multipartFile -> {
                AvatarStorageData avatarStorageData = this.amazonS3Client.uploadFile(multipartFile, communityId);
                PostMedia postMedia = new PostMedia();
                postMedia.setMediaKey(avatarStorageData.getKey());
                return postMedia;
            }).collect(Collectors.toList());
            postAction.setCommunity(communityCommonService.getCommunity(communityId));
            postAction.setPostMediaList(postMediaList);
            return postActionService.creatPostAction(postAction);
        } else {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public PostAction creatPostAction(PostAction postAction, long communityId) {
        if (postAction != null) {
            if (postAction.getText().length() == 0) {
                throw new InvalidRequestException("Text can not be empty!");
            }
            postAction.setCommunity(communityCommonService.getCommunity(communityId));
            return postActionService.creatPostAction(postAction);
        } else {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public void updatePostAction(Long communityId, Long postId, PostAction postActionRequest) {
        PostAction post = postActionRepository.findByPostActionIdAndCommunity(postId, communityCommonService.getCommunity(communityId));
        if (post != null) {
            postActionRequest.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            postActionRepository.save(postActionRequest);
        } else {
            throw new ResourceNotFoundException("Post not found!");
        }
    }

    public void deletePostAction(Long communityId, Long postId) {
        PostAction post = postActionRepository.findByPostActionIdAndCommunity(postId, communityCommonService.getCommunity(communityId));
        if (post != null) {
            post.getPostMediaList().stream().map(postMedia ->
                    this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(communityCommonService.joinPathToFile(postMedia.getMediaKey(), communityId))
            );
            postActionRepository.delete(post);
        } else {
            throw new ResourceNotFoundException("Post  not found!");
        }
    }
}
