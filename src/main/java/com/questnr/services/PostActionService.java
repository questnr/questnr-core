package com.questnr.services;

import com.questnr.access.PostActionAccessService;
import com.questnr.common.enums.*;
import com.questnr.common.message.helper.messages.PostActionMessages;
import com.questnr.common.message.helper.messages.PostPollAnswerMessages;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.post.normal.PostActionFeedDTO;
import com.questnr.model.dto.post.normal.PostActionUpdateRequestDTO;
import com.questnr.model.dto.post.question.PostPollQuestionMetaDTO;
import com.questnr.model.entities.*;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.model.mapper.PostPollQuestionMetaMapper;
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
    PostPollQuestionMetaMapper postPollQuestionMetaMapper;

    @Autowired
    PostPollQuestionRepository postPollQuestionRepository;

    @Autowired
    PostPollAnswerRepository postPollAnswerRepository;

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    PostActionMapper postActionMapper;

    @Autowired
    PostActionAccessService postActionAccessService;

    PostActionService() {
        postReportMapper = Mappers.getMapper(PostReportMapper.class);
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    public PostAction getPostActionById(Long postActionId) {
        PostAction postAction = postActionRepository.findByPostActionId(postActionId);
        if (postAction != null) {
            return postAction;
        }
        throw new ResourceNotFoundException(PostActionMessages.PA101);
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

    private List<String> makeChunkFromText(String text, boolean preprocess, int maxChunk, int maxLengthOfWord) {
        String newText = preprocess && text != null && text.length() > 0 ? text.toLowerCase() : text;
        List<String> titleChunks = Arrays.asList(newText.split("\\s"));
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

    private List<String> makeChunkFromText(String text, int maxChunk, int maxLengthOfWord) {
        return this.makeChunkFromText(text, true, maxChunk, maxLengthOfWord);
    }

    private String createPostActionSlug(PostAction postAction) {
        long timeStamp = new Date().getTime();
        return postAction.getUserActor().getUsername().toLowerCase() +
                "_" +
                CommonService.removeSpecialCharacters(String.join("-",
                        this.makeChunkFromText(postAction.getText(), 5, 30))) +
                "-" +
                secureRandomService.getSecureRandom().toString() +
                "-" +
                Long.toString(timeStamp).substring(Long.toString(timeStamp).length() - 6, Long.toString(timeStamp).length() - 1);
    }

    private String createPostActionBlogSlug(String blogTitle) {
        long timeStamp = new Date().getTime();
        return CommonService.removeSpecialCharacters(String.join("-",
                this.makeChunkFromText(blogTitle, 5, 30))) +
                "-" +
                secureRandomService.getSecureRandom().toString() +
                "-" +
                Long.toString(timeStamp).substring(Long.toString(timeStamp).length() - 6, Long.toString(timeStamp).length() - 1);
    }

    private String getPostActionTitleTag(PostAction postAction) {
        return this.getPostActionTitleTag(postAction.getText());
    }

    public String getPostActionTitleTag(String postText) {
        // Remove html tags
        String postActionText = postText.replaceAll("\\<.*?\\>", "");
        return CommonService.removeSpecialCharacters(String.join(" ", this.makeChunkFromText(postActionText, false, 10, 30)));
    }

    public PostAction creatPostAction(PostAction postAction, PostType postType) {
        try {
            User user = userCommonService.getUser();
            postAction.setUserActor(user);
            if (postAction.getPostEditorType() == PostEditorType.blog) {
                String blogTitle;
                if (CommonService.isNull(postAction.getBlogTitle()) && postAction.getBlogTitle().length() < 1) {
                    postAction.setTags(this.getPostActionTitleTag(postAction));
                    blogTitle = postAction.getTags().substring(0, Math.min(postAction.getTags().length(), 20));
                    postAction.setBlogTitle(blogTitle + "...");
                } else {
                    blogTitle = this.getPostActionTitleTag(postAction.getBlogTitle());
                    postAction.setTags(blogTitle);
                }
                postAction.setSlug(this.createPostActionBlogSlug(blogTitle));
            } else if (postAction.getPostEditorType() == PostEditorType.normal) {
                postAction.setTags(this.getPostActionTitleTag(postAction));
                postAction.setHashTags(this.parsePostText(postAction.getText()));
                postAction.setBlogTitle(null);
                postAction.setSlug(this.createPostActionSlug(postAction));
            }
            postAction.addMetadata();
            postAction.setStatus(PublishStatus.publish);
            postAction.setFeatured(false);
            postAction.setPopular(false);
            postAction.setPostType(postType);
            postAction.setPostDate(Timestamp.valueOf(LocalDateTime.now()));
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
        throw new InvalidRequestException();
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
        throw new ResourceNotFoundException(PostActionMessages.PA100);
    }

    public PostAction getPostActionMediaList(Long postActionId) {
        return postActionRepository.findByPostActionId(postActionId);
    }

    public PostActionFeedDTO updatePostAction(Long postActionId, PostActionUpdateRequestDTO postActionRequest) {
        return this.updatePostAction(this.getPostActionUsingId(postActionId), postActionRequest);
    }

    public PostActionFeedDTO updatePostAction(Long postActionId, Long communityId, PostActionUpdateRequestDTO postActionRequest) {
        PostAction postAction = postActionRepository.findByPostActionIdAndCommunity(postActionId, communityCommonService.getCommunity(communityId));
        return this.updatePostAction(postAction, postActionRequest);
    }

    public PostActionFeedDTO updatePostAction(PostAction postAction, PostActionUpdateRequestDTO postActionRequest) {
        postActionAccessService.commonPostAccessStrategy(postAction);
        try {
            postAction.setText(postActionRequest.getText());
            postAction.setHashTags(this.parsePostText(postActionRequest.getText()));
            postAction.setStatus(postActionRequest.getStatus());
            if (postAction.getPostEditorType() == PostEditorType.blog && !CommonService.isNull(postActionRequest.getBlogTitle())) {
                postAction.setBlogTitle(postActionRequest.getBlogTitle());
            }
            postAction.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            return postActionMapper.toPostActionFeedDTO(postActionRepository.save(postAction), PostActionType.normal, null);
        } catch (Exception e) {
            throw new InvalidRequestException();
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
            throw new InvalidRequestException(PostActionMessages.PA101);
        }
    }

    public PostAction getPostActionUsingId(Long postId) {
        PostAction postAction = postActionRepository.findByPostActionId(postId);
        if (postAction != null) {
            return postAction;
        }
        throw new ResourceNotFoundException(PostActionMessages.PA101);
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
            throw new InvalidRequestException();
        }
    }

    public PostPollQuestionMetaDTO createPollAnswerPost(PostAction postAction, PostPollAnswerRequest postPollAnswerRequest) {
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
                postPollQuestionRepository.save(postPollQuestion);

                // Notification job created and assigned to Notification Processor.
                notificationJob.createNotificationJob(postPollAnswerRepository.findFirstByPostActionAndUserActor(postAction, user));

                return PostPollQuestionMetaMapper.getMetaMapper(postAction, userCommonService, postPollAnswerRepository);
            } else {
                throw new InvalidRequestException(PostPollAnswerMessages.PPA102);
            }
        } else {
            throw new InvalidRequestException();
        }
    }
}
