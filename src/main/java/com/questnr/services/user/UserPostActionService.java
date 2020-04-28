package com.questnr.services.user;

import com.questnr.exceptions.InvalidInputException;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.dto.PostActionUpdateRequestDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostMedia;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.responses.AvatarStorageData;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.PostActionService;
import com.questnr.services.community.CommunityCommonService;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    final PostActionMapper postActionMapper;

    UserPostActionService() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    public Page<PostActionDTO> getAllPostActionsByUserId(Pageable pageable) {
        User user = userCommonService.getUser();
        try {
            Page<PostAction> postActionPage = postActionRepository.findAllByUserActorOrderByCreatedAtDesc(user, pageable);
            return new PageImpl<>(postActionMapper.toDTOs(postActionPage.getContent()), pageable, postActionPage.getTotalElements());
        } catch (Exception e) {
            LOGGER.error(PostAction.class.getName() + " Exception Occurred");
            throw new InvalidInputException(UserPostActionService.class.getName(), null, null);
        }
    }

    public PostActionDTO creatPostAction(PostAction postAction, List<MultipartFile> files) {
        User user = userCommonService.getUser();
        if (postAction != null) {
            postAction.setUserActor(user);
            List<PostMedia> postMediaList;
            postMediaList = files.stream().map(multipartFile -> {
                AvatarStorageData avatarStorageData = this.amazonS3Client.uploadFile(multipartFile);
                PostMedia postMedia = new PostMedia();
                postMedia.setMediaKey(avatarStorageData.getKey());
                postMedia.setMediaType(avatarStorageData.getMediaType());
                return postMedia;
            }).collect(Collectors.toList());
            postAction.setPostMediaList(postMediaList);
            return postActionMapper.toDTO(postActionService.creatPostAction(postAction));
        } else {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public PostActionDTO creatPostAction(PostAction postAction) {
        User user = userCommonService.getUser();
        if (postAction != null) {
            if (postAction.getText().length() == 0) {
                throw new InvalidRequestException("Text can not be empty!");
            }
            postAction.setUserActor(user);
            return postActionMapper.toDTO(postActionService.creatPostAction(postAction));
        } else {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public void updatePostAction(Long postId, PostActionUpdateRequestDTO postActionRequest) {
        User user = userCommonService.getUser();
        postActionRepository.findByPostActionIdAndUserActor(postId, user).map(post -> {
            post.setText(postActionRequest.getText());
            post.setHashTags(postActionService.parsePostText(postActionRequest.getText()));
            post.setStatus(postActionRequest.getStatus());
            post.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            return postActionRepository.save(post);
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
