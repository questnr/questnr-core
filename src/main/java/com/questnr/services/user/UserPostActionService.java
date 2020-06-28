package com.questnr.services.user;

import com.questnr.common.enums.PostActionType;
import com.questnr.common.enums.PostType;
import com.questnr.common.enums.ResourceType;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.model.dto.*;
import com.questnr.model.entities.*;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.model.mapper.PostPollQuestionMapper;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.PostPollQuestionRepository;
import com.questnr.requests.PostPollAnswerRequest;
import com.questnr.requests.PostPollQuestionRequest;
import com.questnr.responses.ResourceStorageData;
import com.questnr.services.AmazonS3Client;
import com.questnr.services.CommonService;
import com.questnr.services.PostActionService;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.util.ImageCompression;
import com.questnr.util.VideoCompression;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    @Autowired
    CommonService commonService;

    @Autowired
    PostPollQuestionMapper postPollQuestionMapper;

    @Autowired
    PostPollQuestionRepository postPollQuestionRepository;

    UserPostActionService() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    public Page<PostBaseDTO> getAllPostsByUserId(User user, Pageable pageable) {
        List<Object[]> postActionList = postActionRepository.getUserPosts(user.getUserId(), pageable.getPageSize() * pageable.getPageNumber(), pageable.getPageSize());

        List<PostBaseDTO> postBaseDTOArrayList = new ArrayList<>();
        for (Object[] object : postActionList) {
            User userWhoShared;
            PostActionType postActionType;
            PostAction postAction = postActionRepository.findByPostActionId(Long.parseLong(object[0].toString()));
            if (Integer.parseInt(object[1].toString()) == 1) {
                userWhoShared = userCommonService.getUser(Long.parseLong(object[2].toString()));
                postActionType = PostActionType.shared;
            } else {
                userWhoShared = null;
                postActionType = PostActionType.normal;
            }
            if (postAction.getPostType() == PostType.simple) {
                postBaseDTOArrayList.add(postActionMapper.toPostActionFeedDTO(
                        postAction,
                        postActionType,
                        userWhoShared
                ));
            } else {
                postBaseDTOArrayList.add(postActionMapper.toPostPollQuestionFeedDTO(
                        postAction,
                        postActionType,
                        userWhoShared
                ));
            }
        }

        return new PageImpl<>(postBaseDTOArrayList, pageable, postBaseDTOArrayList.size());
    }

    public Page<PostPollQuestionFeedDTO> getAllPostPollQuestion(User user, Pageable pageable) {
        List<Object[]> postActionList = postActionRepository.getAllPostPollQuestion(user.getUserId(), pageable.getPageSize() * pageable.getPageNumber(), pageable.getPageSize());

        List<PostPollQuestionFeedDTO> postPollQuestionFeedDTOS = new ArrayList<>();
        for (Object[] object : postActionList) {
            User userWhoShared;
            PostActionType postActionType;
            PostAction postAction = postActionRepository.findByPostActionId(Long.parseLong(object[0].toString()));
            if (Integer.parseInt(object[1].toString()) == 1) {
                userWhoShared = userCommonService.getUser(Long.parseLong(object[2].toString()));
                postActionType = PostActionType.shared;
            } else {
                userWhoShared = null;
                postActionType = PostActionType.normal;
            }
            postPollQuestionFeedDTOS.add(postActionMapper.toPostPollQuestionFeedDTO(
                    postAction,
                    postActionType,
                    userWhoShared
            ));
        }

        return new PageImpl<>(postPollQuestionFeedDTOS, pageable, postPollQuestionFeedDTOS.size());
    }

    public PostActionFeedDTO creatPostAction(PostAction postAction, List<MultipartFile> files) {
        User user = userCommonService.getUser();
        if (postAction != null) {
            postAction.setUserActor(user);
            List<PostMedia> postMediaList;
            postMediaList = files.stream().map(multipartFile -> {
                ResourceStorageData resourceStorageData = new ResourceStorageData();
                try {
                    File file = commonService.convertMultiPartToFile(multipartFile);
                    if (commonService.checkIfFileIsImage(file)) {
                        try {
                            if (commonService.getFileExtension(file).equals("png")) {
                                resourceStorageData = this.amazonS3Client.uploadFile(file);
                                resourceStorageData.setResourceType(ResourceType.image);
                            } else {
                                ImageCompression imageCompression = new ImageCompression();
                                imageCompression.setInputFile(file);
                                File compressedFile = imageCompression.doCompression();
                                if (file.exists()) file.delete();
                                resourceStorageData = this.amazonS3Client.uploadFile(compressedFile);
                                resourceStorageData.setResourceType(ResourceType.image);
                            }
                        } catch (Exception e) {

                        }
                    } else {
                        String fileName = "out_" + commonService.generateFileName(file);
                        File target = new File(fileName);
                        try {
                            VideoCompression videoCompression = new VideoCompression(file, target);
                            Thread videoCompressionThread = new Thread(videoCompression, fileName);
                            videoCompressionThread.start();
                            videoCompressionThread.join();
                            if (file.exists()) file.delete();
                            resourceStorageData = this.amazonS3Client.uploadFile(target);
                            resourceStorageData.setResourceType(ResourceType.video);
                        } catch (InterruptedException e) {

                        }
                    }
                    if (resourceStorageData.getKey() != null && !CommonService.isNull(resourceStorageData.getKey())) {
                        PostMedia postMedia = new PostMedia();
                        postMedia.setMediaKey(resourceStorageData.getKey());
                        postMedia.setResourceType(resourceStorageData.getResourceType());
                        return postMedia;
                    }
                } catch (IOException ex) {

                }
                return null;
            }).collect(Collectors.toList());
            postAction.setPostMediaList(postMediaList);
            return postActionMapper.toPostActionFeedDTO(postActionService.creatPostAction(postAction), PostActionType.normal, null);
        } else {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public PostActionFeedDTO creatPostAction(PostAction postAction) {
        User user = userCommonService.getUser();
        if (postAction != null) {
            if (postAction.getText().length() == 0) {
                throw new InvalidRequestException("Text can not be empty!");
            }
            postAction.setUserActor(user);
            return postActionMapper.toPostActionFeedDTO(postActionService.creatPostAction(postAction), PostActionType.normal, null);
        } else {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public PostPollQuestionForCommunityDTO createPollQuestionPost(PostPollQuestionRequest postPollQuestionRequest) {
        if (postPollQuestionRequest != null) {
            PostAction postAction = new PostAction();
            postAction.setText(postPollQuestionRequest.getText());
            PostPollQuestion pollQuestionAction = postPollQuestionMapper.fromRequest(postPollQuestionRequest);
            PostAction savedPostAction = postActionService.creatPostAction(postAction, PostType.question);
            savedPostAction.setPostPollQuestion(pollQuestionAction);
            return postActionMapper.toPostPollQuestionForCommunityDTO(postActionRepository.save(savedPostAction));
        } else {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public PostPollQuestionDTO createPollAnswerPost(PostAction postAction, PostPollAnswerRequest postPollAnswerRequest) {
        if (postPollAnswerRequest != null) {
            PostPollAnswer postPollAnswer = new PostPollAnswer();
            postPollAnswer.setAnswer(postPollAnswerRequest.getPollAnswer());
            PostPollQuestion postPollQuestion = postAction.getPostPollQuestion();
            postPollQuestion.getPostPollAnswer().add(postPollAnswer);
            return postPollQuestionMapper.toDTO(postPollQuestionRepository.save(postPollQuestion));
        } else {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }
}
