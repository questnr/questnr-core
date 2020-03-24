package com.questnr.services.user;

import com.questnr.exceptions.InvalidInputException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostMedia;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.responses.AvatarStorageData;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.PostActionService;
import com.questnr.services.community.CommunityCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPostActionService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    PostActionService postActionService;

    @Autowired
    PostActionRepository postActionRepository;

    UserCommonService userCommonService;

    public Page<PostAction> getAllPostActionsByUserId(Pageable pageable) {
        User user = userCommonService.getUser();
        if (user != null) {
            try {
                return postActionRepository.findAllByUserActorOrderByCreatedAtDesc(user, pageable);
            } catch (Exception e) {
                LOGGER.error(PostAction.class.getName() + " Exception Occurred");
            }
        } else {
            throw new InvalidInputException(User.class.getName(), null, null);
        }
        return null;
    }

    public PostAction creatPostAction(PostAction postAction, List<MultipartFile> files) {
        if (postAction != null) {
            List<PostMedia> postMediaList;
            postMediaList = files.stream().map(multipartFile -> {
                AvatarStorageData avatarStorageData = this.amazonS3Client.uploadFile(multipartFile);
                PostMedia postMedia = new PostMedia();
                postMedia.setMediaKey(avatarStorageData.getKey());
                return postMedia;
            }).collect(Collectors.toList());
            return postActionService.creatPostAction(postAction, postMediaList);
        } else {
            throw new InvalidInputException(PostAction.class.getName(), null, null);
        }
    }

    public void updatePostAction(Long postId, PostAction postActionRequest) {
        User user = userCommonService.getUser();
        postActionRepository.findById(postId).map(post -> {
            postActionRequest.setUserActor(user);
            postActionRequest.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            return postActionRepository.save(postActionRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Post not found!"));
    }

    public void deletePostAction(Long postId) {
        postActionRepository.findById(postId).map(post -> {
            post.getPostMediaList().stream().map(postMedia ->
                    this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(userCommonService.joinPathToFile(postMedia.getMediaKey()))
            );
            postActionRepository.delete(post);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Post  not found!"));
    }
}
