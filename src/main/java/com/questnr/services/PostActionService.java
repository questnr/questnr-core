package com.questnr.services;

import com.questnr.common.enums.PostActionPrivacy;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.PostActionSharableLinkDTO;
import com.questnr.model.entities.HashTag;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostMedia;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.HashTagRepository;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.services.user.UserCommonService;
import com.questnr.util.SecureRandomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
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

    @Value("${questNR.domain}")
    String QUEST_NR_DOMAIN;

    final private String POST_ACTION_PATH = "posts";

    private String createPostActionSlug(PostAction postAction) {
        Long timeStamp = new Date().getTime();
        List<String> titleChunks = Arrays.asList(postAction.getTitle().toLowerCase().split("\\s"));
        int maxTitleChunk = titleChunks.size();
        if (maxTitleChunk > 9) {
            maxTitleChunk = 9;
        }
        return postAction.getUserActor().getUsername() +
                "_" +
                String.join("-", (titleChunks.subList(0, maxTitleChunk))).replaceAll("[ ](?=[ ])|[^-_A-Za-z0-9 ]+", "") +
                "-" +
                secureRandomService.getSecureRandom().toString() +
                "-" +
                timeStamp.toString().substring(1, 5);
    }

    private String getPostActionTitleTag(PostAction postAction) {
        List<String> titleChunks = Arrays.asList(postAction.getTitle().toLowerCase().split("\\s"));
        return String.join(" ", titleChunks.subList(0, titleChunks.size())).replaceAll("[ ](?=[ ])|[^A-Za-z0-9 ]+", "");
    }

    public PostAction creatPostAction(PostAction postAction, List<PostMedia> postMediaList) {
        try {
            User user = userCommonService.getUser();
            postAction.addMetadata();
            postAction.setHashTags(this.parsePostText(postAction.getText()));
            postAction.setUserActor(user);
            postAction.setPostDate(Timestamp.valueOf(LocalDateTime.now()));
            postAction.setPostMediaList(postMediaList);
            postAction.setSlug(this.createPostActionSlug(postAction));
            postAction.setTitleTag(this.getPostActionTitleTag(postAction));
            if (postAction.getPostActionPrivacy() == null) {
                postAction.setPostActionPrivacy(PostActionPrivacy.public_post);
            }
            return postActionRepository.saveAndFlush(postAction);
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
                    // @Todo: Check if the hashTag exits
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

    public PostActionSharableLinkDTO getPostActionSharableLink(Long postActionId) {
        PostActionSharableLinkDTO postActionSharableLinkDTO = new PostActionSharableLinkDTO();
        String postActionSlug = postActionRepository.findByPostActionId(postActionId).getSlug();
        String sharableLink = QUEST_NR_DOMAIN+"/"+Paths.get(POST_ACTION_PATH, postActionSlug).toString();
        postActionSharableLinkDTO.setPostActionLink(sharableLink);
        return postActionSharableLinkDTO;
    }
}
