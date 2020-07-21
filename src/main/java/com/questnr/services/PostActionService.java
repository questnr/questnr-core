package com.questnr.services;

import com.questnr.common.enums.NotificationType;
import com.questnr.common.enums.PostActionPrivacy;
import com.questnr.common.enums.PostType;
import com.questnr.common.enums.PublishStatus;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.post.normal.PostActionUpdateRequestDTO;
import com.questnr.model.dto.post.question.PollQuestionDTO;
import com.questnr.model.entities.*;
import com.questnr.model.mapper.PostPollQuestionMapper;
import com.questnr.model.mapper.PostReportMapper;
import com.questnr.model.repositories.*;
import com.questnr.requests.PostPollAnswerRequest;
import com.questnr.requests.PostReportRequest;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.notification.NotificationJob;
import com.questnr.services.user.UserCommonService;
import com.questnr.util.SecureRandomService;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostActionService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    CommonService commonService;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    HashTagRepository hashTagRepository;

    @Autowired
    SecureRandomService secureRandomService;

    @Autowired
    NotificationJob notificationJob;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    PostActionTrendLinearDataRepository postActionTrendLinearDataRepository;

    @Autowired
    PostReportRepository postReportRepository;

    @Autowired
    PostReportMapper postReportMapper;

    @Autowired
    PostPollQuestionMapper postPollQuestionMapper;

    @Autowired
    PostPollQuestionRepository postPollQuestionRepository;

    @Autowired
    PostPollAnswerRepository postPollAnswerRepository;

    @Autowired
    CommunityCommonService communityCommonService;

    PostActionService() {
        postReportMapper = Mappers.getMapper(PostReportMapper.class);
    }

    public PostAction getPostActionById(Long postActionId) {
        PostAction postAction = postActionRepository.findByPostActionId(postActionId);
        if (postAction != null) {
            return postAction;
        }
        throw new ResourceNotFoundException("Post not found!");
    }

    public boolean isPostActionBelongsToCommunity(Long postActionId) {
        return this.isPostActionBelongsToCommunity(this.getPostActionUsingId(postActionId));
    }

    public boolean isPostActionBelongsToCommunity(PostAction postAction) {
        return postAction != null && postAction.getCommunity() != null && !commonService.isNull(postAction.getCommunity().getCommunityId().toString());
    }

    public PostAction getPostActionByIdAndType(Long postActionId, PostType postType) {
        PostAction postAction = postActionRepository.findByPostActionIdAndPostType(postActionId, postType);
        if (postAction != null) {
            return postAction;
        }
        throw new ResourceNotFoundException("Post not found!");
    }

    private List<String> makeChunkFromText(String text, int maxChunk, int maxLengthOfWord) {
        List<String> titleChunks = Arrays.asList(text.toLowerCase().split("\\s"));
        int maxTitleChunk = titleChunks.size();
        if (maxTitleChunk > maxChunk) {
            maxTitleChunk = maxChunk;
        }
        titleChunks = titleChunks.subList(0, maxTitleChunk);
        List<String> finalChunks = new ArrayList<>();
        for (String chunk : titleChunks) {
            if (chunk.length() > maxLengthOfWord) {
                finalChunks.add(chunk.substring(0, maxLengthOfWord));
            } else {
                finalChunks.add(chunk);
            }
        }
//        if (titleChunks.get(0).length() > maxLengthOfSlug || avoidMaxLengthOfSlug) {
//            finalChunks.add(titleChunks.get(0).substring(0, maxLengthOfSlug));
//        } else {
//            for (String chunk : titleChunks) {
//                if (chunk.length() > maxLengthOfWord) {
//                    finalChunks.add(chunk.substring(0, maxLengthOfWord));
//                } else {
//                    finalChunks.add(chunk);
//                }
//            }
//        }
        return finalChunks;
    }

    private String createPostActionSlug(PostAction postAction) {
        Long timeStamp = new Date().getTime();
        return postAction.getUserActor().getUsername().toLowerCase() +
                "_" +
                CommonService.removeSpecialCharacters(String.join("-", this.makeChunkFromText(this.getPostActionTitleTag(postAction), 5, 10))) +
                "-" +
                secureRandomService.getSecureRandom().toString() +
                "-" +
                timeStamp.toString().substring(timeStamp.toString().length() - 6, timeStamp.toString().length() - 1);
    }

    private String getPostActionTitleTag(PostAction postAction) {
        return this.getPostActionTitleTag(postAction.getText());
    }

    public String getPostActionTitleTag(String postText) {
        // Remove html tags
        String postActionText = postText.replaceAll("\\<.*?\\>", "");
        return CommonService.removeSpecialCharacters(String.join(" ", this.makeChunkFromText(postActionText, 10, 10)));
    }

    public PostAction creatPostAction(PostAction postAction, PostType postType) {
        try {
            User user = userCommonService.getUser();
            postAction.addMetadata();
            postAction.setHashTags(this.parsePostText(postAction.getText()));
            postAction.setUserActor(user);
            postAction.setPostDate(Timestamp.valueOf(LocalDateTime.now()));
            postAction.setSlug(this.createPostActionSlug(postAction));
            postAction.setTags(this.getPostActionTitleTag(postAction));
            postAction.setStatus(PublishStatus.publish);
            postAction.setFeatured(false);
            postAction.setPopular(false);
            postAction.setPostType(postType);
            if (postAction.getPostActionPrivacy() == null) {
                postAction.setPostActionPrivacy(PostActionPrivacy.public_post);
            }
            PostAction savedPostAction = postActionRepository.save(postAction);
            // Notification job created and assigned to Notification Processor.
            notificationJob.createNotificationJob(savedPostAction);

            return savedPostAction;
        } catch (Exception e) {
            LOGGER.error(PostAction.class.getName() + " Exception Occurred");
        }
        throw new InvalidRequestException("Error occurred. Please, try again!");
    }

    public PostAction creatPostAction(PostAction postAction) {
        return this.creatPostAction(postAction, PostType.simple);
    }

    public Set<HashTag> parsePostText(String postText) {
        User user = userCommonService.getUser();
        Set<HashTag> hashTags = new HashSet<>();
        StringTokenizer tokenizer = new StringTokenizer(postText);

        while (tokenizer.hasMoreTokens()) {
            String hashToken = tokenizer.nextToken();
            if (hashToken.startsWith("#")) {
                hashToken = hashToken.replaceAll("#", "").trim();
                if (!hashToken.equals("")) {
                    HashTag hashTag = hashTagRepository.findByHashTagValue(hashToken.toLowerCase());
                    if (hashTag != null) {
                        hashTags.add(hashTag);
                    } else {
                        hashTag = new HashTag();
                        hashTag.setHashTagValue(hashToken.toLowerCase());
                        hashTag.setUserCreator(user);
                        hashTag.addMetadata();
                        hashTags.add(hashTagRepository.save(hashTag));
                    }
                }
            }
        }
        return hashTags;
    }

    public PostAction getPostActionFromSlug(String postActionSlug) {
        PostAction postAction = postActionRepository.findFirstBySlug(postActionSlug);
        if (postAction != null) {
            return postAction;
        }
        throw new ResourceNotFoundException("Post is deleted or link is not correct");
    }

    public PostAction getPostActionMediaList(Long postActionId) {
        return postActionRepository.findByPostActionId(postActionId);
    }

    public void updatePostAction(Long postActionId, PostActionUpdateRequestDTO postActionRequest) {
        this.updatePostAction(this.getPostActionUsingId(postActionId), postActionRequest);
    }

    public void updatePostAction(Long postActionId, Long communityId, PostActionUpdateRequestDTO postActionRequest) {
        PostAction postAction = postActionRepository.findByPostActionIdAndCommunity(postActionId, communityCommonService.getCommunity(communityId));
        this.updatePostAction(postAction, postActionRequest);
    }

    public void updatePostAction(PostAction postAction, PostActionUpdateRequestDTO postActionRequest) {
        try {
            postAction.setText(postActionRequest.getText());
            postAction.setHashTags(this.parsePostText(postActionRequest.getText()));
            postAction.setStatus(postActionRequest.getStatus());
            postAction.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            postActionRepository.save(postAction);
        } catch (Exception e) {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public void deletePostAction(Long postActionId, Long communityId) {
        PostAction postAction = postActionRepository.findByPostActionIdAndCommunity(postActionId, communityCommonService.getCommunity(communityId));
        this.deletePostAction(postAction);
    }

    public void deletePostAction(Long postActionId) {
        this.deletePostAction(this.getPostActionUsingId(postActionId));
    }

    public void deletePostAction(PostAction postAction) {
        if (postAction != null) {
            try {
                PostActionTrendLinearData postActionTrendLinearData = postActionTrendLinearDataRepository.findByPostAction(postAction);
                if (postActionTrendLinearData != null) {
                    postActionTrendLinearDataRepository.delete(postActionTrendLinearData);
                }
                notificationRepository.deleteByNotificationBaseAndType(
                        postAction.getPostActionId(),
                        NotificationType.post.getJsonValue()
                );
            } catch (Exception e) {

            }
            postAction.setDeleted(true);
            postActionRepository.save(postAction);
        } else {
            throw new InvalidRequestException("Post not found!");
        }
    }

    public PostAction getPostActionUsingId(Long postId) {
        PostAction postAction = postActionRepository.findByPostActionId(postId);
        if (postAction != null) {
            return postAction;
        }
        throw new ResourceNotFoundException("Post not found!");
    }

    public void reportPost(Long postId, PostReportRequest postReportRequest) {
        User user = userCommonService.getUser();
        PostAction postAction = this.getPostActionUsingId(postId);
        try {
            PostReport postReport = postReportMapper.fromPostReportRequest(postReportRequest);
            postReport.setUserActor(user);
            postReport.setPostAction(postAction);
            postReport.addMetadata();
            this.postReportRepository.save(postReport);
        } catch (Exception e) {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public PollQuestionDTO createPollAnswerPost(PostAction postAction, PostPollAnswerRequest postPollAnswerRequest) {
        User user = userCommonService.getUser();
        if (postPollAnswerRequest != null) {
            if (!postPollAnswerRepository.existsByPostActionAndUserActor(postAction, user)) {
                PostPollAnswer postPollAnswer = new PostPollAnswer();
                postPollAnswer.setAnswer(postPollAnswerRequest.getPollAnswer());
                postPollAnswer.setUserActor(user);
                postPollAnswer.setPostAction(postAction);
                postPollAnswer.addMetadata();
                PostPollQuestion postPollQuestion = postAction.getPostPollQuestion();
                postPollQuestion.getPostPollAnswer().add(postPollAnswer);
                PostPollQuestion savedPostPollQuestion = postPollQuestionRepository.save(postPollQuestion);

                // Notification job created and assigned to Notification Processor.
                notificationJob.createNotificationJob(postPollAnswerRepository.findFirstByPostActionAndUserActor(postAction, user));

                return postPollQuestionMapper.toDTO(savedPostPollQuestion);
            } else {
                throw new InvalidRequestException("Already submitted the answer");
            }
        } else {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }
}
