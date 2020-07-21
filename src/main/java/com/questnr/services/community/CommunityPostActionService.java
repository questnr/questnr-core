package com.questnr.services.community;

import com.questnr.common.enums.PostActionType;
import com.questnr.common.enums.PostType;
import com.questnr.common.enums.ResourceType;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.post.PostBaseDTO;
import com.questnr.model.dto.post.normal.PostActionForCommunityDTO;
import com.questnr.model.dto.post.question.PollQuestionDTO;
import com.questnr.model.dto.post.question.PostPollQuestionForCommunityDTO;
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
import com.questnr.services.user.UserCommonService;
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
    PostActionMapper postActionMapper;

    @Autowired
    PostPollQuestionMapper postPollQuestionMapper;

    @Autowired
    CommonService commonService;

    @Autowired
    PostPollQuestionRepository postPollQuestionRepository;

    CommunityPostActionService() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    public Page<PostBaseDTO> getAllPostActionsByCommunityId(long communityId, Pageable pageable) {
        Community community = communityCommonService.getCommunity(communityId);
        if (community != null) {
            List<Object[]> postActionList = postActionRepository.findAllByCommunityPosts(community.getCommunityId(),
                    pageable.getPageSize() * pageable.getPageNumber(), pageable.getPageSize());
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
        throw new ResourceNotFoundException("Community not found!");
    }

    public PostActionForCommunityDTO creatPostAction(PostAction postAction, List<MultipartFile> files, long communityId) {
        if (postAction != null) {
            List<PostMedia> postMediaList;
            postMediaList = files.stream().map(multipartFile -> {
                ResourceStorageData resourceStorageData = new ResourceStorageData();
                try {
                    File file = commonService.convertMultiPartToFile(multipartFile);
                    if (commonService.checkIfFileIsImage(file)) {
                        try {
                            if (commonService.getFileExtension(file).equals("png")) {
                                resourceStorageData = this.amazonS3Client.uploadFile(file, communityId);
                                resourceStorageData.setResourceType(ResourceType.image);
                            } else {
                                ImageCompression imageCompression = new ImageCompression();
                                imageCompression.setInputFile(file);
                                File compressedFile = imageCompression.doCompression();
                                if (file.exists()) file.delete();
                                resourceStorageData = this.amazonS3Client.uploadFile(compressedFile, communityId);
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
                            resourceStorageData = this.amazonS3Client.uploadFile(target, communityId);
                            resourceStorageData.setResourceType(ResourceType.video);
                            if (file.exists()) file.delete();
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

    public PostPollQuestionForCommunityDTO createPollQuestionPost(Long communityId, PostPollQuestionRequest postPollQuestionRequest) {
        if (postPollQuestionRequest != null) {
            Community community = communityCommonService.getCommunity(communityId);
            PostAction postAction = new PostAction();
            postAction.setText(postPollQuestionRequest.getText());
            postAction.setCommunity(community);
            PostPollQuestion postPollQuestion = postPollQuestionMapper.fromRequest(postPollQuestionRequest);
            PostAction savedPostAction = postActionService.creatPostAction(postAction, PostType.question);
            savedPostAction.setPostPollQuestion(postPollQuestion);
            return postActionMapper.toPostPollQuestionForCommunityDTO(postActionRepository.save(savedPostAction));
        } else {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public PollQuestionDTO createPollAnswerPost(Long postActionId, PostPollAnswerRequest postPollAnswerRequest) {
        return this.postActionService.createPollAnswerPost(postActionService.getPostActionByIdAndType(postActionId, PostType.question), postPollAnswerRequest);
    }

    public Page<PostAction> getAllPostPollQuestion(Long communityId, Pageable pageable) {
        Community community = communityCommonService.getCommunity(communityId);
        if (community != null)
            return postActionRepository.findAllByCommunityAndPostTypeOrderByCreatedAtDesc(community,
                    PostType.question, pageable);
        throw new ResourceNotFoundException("Community not found!");
    }
}
