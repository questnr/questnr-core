package com.questnr.services;

import com.questnr.common.enums.NotificationType;
import com.questnr.common.enums.PostActionPrivacy;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.PostActionUpdateRequestDTO;
import com.questnr.model.entities.HashTag;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.HashTagRepository;
import com.questnr.model.repositories.NotificationRepository;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.services.notification.NotificationJob;
import com.questnr.services.user.UserCommonService;
import com.questnr.util.SecureRandomService;
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
                CommonService.removeSpecialCharacters(String.join("-", this.makeChunkFromText(postAction.getText(), 5, 10))) +
                "-" +
                secureRandomService.getSecureRandom().toString() +
                "-" +
                timeStamp.toString().substring(timeStamp.toString().length() - 6, timeStamp.toString().length() - 1);
    }

    private String getPostActionTitleTag(PostAction postAction) {
        return CommonService.removeSpecialCharacters(String.join(" ", this.makeChunkFromText(postAction.getText(), 10, 10)));
    }

    public PostAction creatPostAction(PostAction postAction) {
        try {
            User user = userCommonService.getUser();
            postAction.addMetadata();
            postAction.setHashTags(this.parsePostText(postAction.getText()));
            postAction.setUserActor(user);
            postAction.setPostDate(Timestamp.valueOf(LocalDateTime.now()));
            postAction.setSlug(this.createPostActionSlug(postAction));
            postAction.setTags(this.getPostActionTitleTag(postAction));
            postAction.setFeatured(false);
            postAction.setPopular(false);
            if (postAction.getPostActionPrivacy() == null) {
                postAction.setPostActionPrivacy(PostActionPrivacy.public_post);
            }
            PostAction savedPostAction = postActionRepository.saveAndFlush(postAction);
            // Notification job created and assigned to Notification Processor.
            notificationJob.createNotificationJob(savedPostAction);

            return savedPostAction;
        } catch (Exception e) {
            LOGGER.error(PostAction.class.getName() + " Exception Occurred");
        }
        throw new InvalidRequestException("Error occurred. Please, try again!");
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

    public void updatePostAction(PostAction post, PostActionUpdateRequestDTO postActionRequest) {
        try {
            post.setText(postActionRequest.getText());
            post.setHashTags(this.parsePostText(postActionRequest.getText()));
            post.setStatus(postActionRequest.getStatus());
            post.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            postActionRepository.save(post);
        } catch (Exception e) {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }

    public void deletePostAction(PostAction postAction) {
        try {
            try {
                notificationRepository.deleteByNotificationBaseAndType(
                        postAction.getPostActionId(),
                        NotificationType.post.getJsonValue()
                );
            } catch (Exception e) {

            }
            postAction.setDeleted(true);
            postActionRepository.save(postAction);
        } catch (Exception e) {
            throw new InvalidRequestException("Error occurred. Please, try again!");
        }
    }
}
