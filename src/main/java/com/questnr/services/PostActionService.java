package com.questnr.services;

import com.questnr.common.enums.PostActionPrivacy;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.PostActionPublicDTO;
import com.questnr.model.dto.PostActionSharableLinkDTO;
import com.questnr.model.entities.*;
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

    @Value("${questnr.domain}")
    String QUEST_NR_DOMAIN;

    final private String POST_ACTION_PATH = "posts";

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

    private PostActionMetaInformation getPostActionDescMetaInformation(PostActionPublicDTO postActionPublicDTO) {
        MetaInformation metaInfo = new MetaInformation();
        metaInfo.setAttributeType("name");
        metaInfo.setType("description");
        metaInfo.setContent(CommonService.removeSpecialCharacters(postActionPublicDTO.getText()));
        PostActionMetaInformation postMeta = new PostActionMetaInformation();
        postMeta.setMetaInformation(metaInfo);
        return postMeta;
    }

    public PostActionPublicDTO setPostActionMetaInformation(PostActionPublicDTO postActionPublicDTO) {
        if (postActionPublicDTO != null) {
            List<PostActionMetaInformation> metaList = new LinkedList<PostActionMetaInformation>();
            if (postActionPublicDTO.getMetaList() == null || postActionPublicDTO.getMetaList().size() == 0) {
                metaList.add(this.getPostActionDescMetaInformation(postActionPublicDTO));
            } else {
                boolean foundDesc = false;
                for (PostActionMetaInformation meta : postActionPublicDTO.getMetaList()) {
                    if (meta != null && meta.getMetaInformation() != null) {
                        if (meta.getMetaInformation().getType().equals("description")) {
                            foundDesc = true;
                            break;
                        }
                    }
                }
                if (!foundDesc) {
                    metaList.add(this.getPostActionDescMetaInformation(postActionPublicDTO));
                }
            }
            postActionPublicDTO.getMetaList().addAll(metaList);

//            if (postAction.getTags() == null || postAction.getTags().isEmpty()) {
//                postAction.setTags(this.getPostActionTitleTag(postAction));
//            }

        }
        return postActionPublicDTO;
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
        String sharableLink = QUEST_NR_DOMAIN + "/" + Paths.get(POST_ACTION_PATH, postActionSlug).toString();
        postActionSharableLinkDTO.setPostActionLink(sharableLink);
        return postActionSharableLinkDTO;
    }

    public PostAction getPostActionMediaList(Long postActionId) {
        return postActionRepository.findByPostActionId(postActionId);
    }
}
