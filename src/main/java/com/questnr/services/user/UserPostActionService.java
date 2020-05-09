package com.questnr.services.user;

import com.questnr.common.enums.PostActionType;
import com.questnr.exceptions.InvalidInputException;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.model.dto.PostActionCardDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    public Page<PostActionCardDTO> getAllPostsByUserSlug(User user, Pageable pageable) {
        List<Object[]> postActionList = postActionRepository.getUserPosts(user.getUserId(), pageable.getPageSize() * pageable.getPageNumber(), pageable.getPageSize());

        List<PostActionCardDTO> postActionCardDTOS = new ArrayList<>();
        for (Object[] object : postActionList) {
            if (Integer.parseInt(object[1].toString()) == 1) {
                postActionCardDTOS.add(postActionMapper.toCardDTO(
                        postActionRepository.findByPostActionId(Long.parseLong(object[0].toString())),
                        PostActionType.shared,
                        userCommonService.getUser(Long.parseLong(object[2].toString()))));
            } else {
                postActionCardDTOS.add(postActionMapper.toCardDTO(
                        postActionRepository.findByPostActionId(Long.parseLong(object[0].toString())),
                        PostActionType.normal,
                        null));
            }
        }

        return new PageImpl<>(postActionCardDTOS, pageable, postActionCardDTOS.size());
    }

    public PostActionCardDTO creatPostAction(PostAction postAction, List<MultipartFile> files) {
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
            return postActionMapper.toCardDTO(postActionService.creatPostAction(postAction), PostActionType.normal, null);
        } else {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public PostActionCardDTO creatPostAction(PostAction postAction) {
        User user = userCommonService.getUser();
        if (postAction != null) {
            if (postAction.getText().length() == 0) {
                throw new InvalidRequestException("Text can not be empty!");
            }
            postAction.setUserActor(user);
            return postActionMapper.toCardDTO(postActionService.creatPostAction(postAction), PostActionType.normal, null);
        } else {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }
}
