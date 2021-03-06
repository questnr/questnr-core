package com.questnr.services.community;

import com.questnr.common.PostMediaHandlingEntity;
import com.questnr.common.enums.PostActionType;
import com.questnr.common.enums.PostMediaHandlingEntityType;
import com.questnr.common.enums.PostType;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.post.PostBaseDTO;
import com.questnr.model.dto.post.normal.PostActionFeedDTO;
import com.questnr.model.dto.post.question.PostPollQuestionForCommunityDTO;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostPollQuestion;
import com.questnr.model.entities.media.PostMedia;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.model.mapper.PostPollQuestionMapper;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.PostPollQuestionRepository;
import com.questnr.requests.PostPollQuestionRequest;
import com.questnr.services.*;
import com.questnr.services.user.UserCommonService;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
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
    PostActionUserViewableService postActionUserViewableService;

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

    @Autowired
    PostMediaService postMediaService;

    CommunityPostActionService() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    public Page<PostBaseDTO> getAllPostActionsByCommunityId(long communityId, Pageable pageable) {
        Community community = communityCommonService.getCommunity(communityId);
        if (community != null) {
            List<Object[]> postActionList = postActionRepository.findAllByCommunityPosts(community.getCommunityId(),
                    pageable.getPageSize() * pageable.getPageNumber(), pageable.getPageSize());
            return this.postActionUserViewableService.getUserViewablePosts(postActionList, pageable);
        }
        throw new ResourceNotFoundException("Community not found!");
    }

    public Page<PostBaseDTO> getCommunityPostsUsingIdList(Long communityId,
                                                          String posts,
                                                          Long lastPostId,
                                                          Pageable pageable) {
        if (lastPostId != null) {
            Community community = communityCommonService.getCommunity(communityId);
            PostAction postAction = postActionService.getPostActionById(lastPostId);
            if (postAction != null && postAction.getCommunity().equals(community)) {
                List<Object[]> postActionList = postActionRepository.findAllByCommunityPostsByLastPosted(
                        community.getCommunityId(),
                        postAction.getCreatedAt());
                List<String> postIdList = postActionList.stream().map(objects ->
                        objects[0].toString()
                ).filter(postId ->
                        !postAction.getPostActionId().equals(Long.parseLong(postId))
                ).collect(Collectors.toList());
                List<String> slicedPostIdList = postIdList.subList(0, Math.min(4, postIdList.size()));
                return this.postActionUserViewableService.getPostBaseDTOPageFromPostId(slicedPostIdList,
                        pageable, postIdList.size());
            }
            throw new ResourceNotFoundException("Community not found!");
        } else {
            List<String> postIdStringList = Arrays.asList(posts.split(","));
            return this.postActionUserViewableService.getPostBaseDTOPageFromPostId(postIdStringList, pageable, communityId);
        }
    }

    public PostActionFeedDTO creatPostAction(PostAction postAction, List<MultipartFile> files, long communityId) {
        if (postAction != null) {
            postAction.setCommunity(communityCommonService.getCommunity(communityId));
            postAction.setPostMediaList(postMediaService.handleFiles(files,
                    new PostMediaHandlingEntity(PostMediaHandlingEntityType.community, communityId)).stream().map(media ->
                    (PostMedia) media
            ).collect(Collectors.toList()));
            return postActionMapper.toPostActionFeedDTO(postActionService.creatPostAction(postAction), PostActionType.normal, null);
        } else {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public PostActionFeedDTO creatPostAction(PostAction postAction, long communityId) {
        if (postAction != null) {
            if (postAction.getText().length() == 0) {
                throw new InvalidRequestException("Text can not be empty!");
            }
            postAction.setCommunity(communityCommonService.getCommunity(communityId));
            return postActionMapper.toPostActionFeedDTO(postActionService.creatPostAction(postAction), PostActionType.normal, null);
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

//    public PollQuestionDTO createPollAnswerPost(Long postActionId, PostPollAnswerRequest postPollAnswerRequest) {
//        return this.postActionService.createPollAnswerPost(postActionService.getPostActionByIdAndType(postActionId, PostType.question), postPollAnswerRequest);
//    }

    public Page<PostAction> getAllPostPollQuestion(Long communityId, Pageable pageable) {
        Community community = communityCommonService.getCommunity(communityId);
        if (community != null)
            return postActionRepository.findAllByCommunityAndPostTypeOrderByCreatedAtDesc(community,
                    PostType.question, pageable);
        throw new ResourceNotFoundException("Community not found!");
    }
}
