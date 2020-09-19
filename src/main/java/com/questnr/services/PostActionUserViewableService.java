package com.questnr.services;

import com.questnr.common.enums.PostActionType;
import com.questnr.common.enums.PostType;
import com.questnr.model.dto.post.PostBaseDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.services.community.CommunityCommonService;
import com.questnr.services.user.UserCommonService;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PostActionUserViewableService {
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
    CommunityCommonService communityCommonService;

    @Autowired
    PostActionMapper postActionMapper;

    PostActionUserViewableService() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    public Page<PostBaseDTO> getUserViewablePosts(List<Object[]> postActionList, Pageable pageable) {
        List<PostBaseDTO> postActionFeedDTOList = new ArrayList<>();
        for (Object[] object : postActionList) {
            User userWhoShared = null;
            PostActionType postActionType = PostActionType.normal;
            if (Integer.parseInt(object[1].toString()) == 1) {
                userWhoShared = userCommonService.getUser(Long.parseLong(object[2].toString()));
                postActionType = PostActionType.shared;
            }
            postActionFeedDTOList.add(
                    this.getPostBaseDTOFromPostId
                            (Long.parseLong(object[0].toString()),
                                    userWhoShared,
                                    postActionType));
        }
        return new PageImpl<>(postActionFeedDTOList, pageable, postActionFeedDTOList.size());
    }

    public PostBaseDTO getPostBaseDTOFromPostId(PostAction postAction,
                                                User userWhoShared,
                                                PostActionType postActionType) {
        if (postAction != null) {
            if (postAction.getPostType() == PostType.simple) {
                return postActionMapper.toPostActionFeedDTO(
                        postAction,
                        postActionType,
                        userWhoShared
                );
            } else {
                return postActionMapper.toPostPollQuestionFeedDTO(
                        postAction,
                        postActionType,
                        userWhoShared
                );
            }
        }
        return null;
    }

    public PostBaseDTO getPostBaseDTOFromPostId(Long postId,
                                                User userWhoShared,
                                                PostActionType postActionType) {
        PostAction postAction = postActionRepository.findByPostActionId(postId);
        return this.getPostBaseDTOFromPostId(postAction, userWhoShared, postActionType);
    }

    public PostBaseDTO getPostBaseDTOFromPostId(Long postId,
                                                User userWhoShared,
                                                PostActionType postActionType,
                                                Long communityId) {
        PostAction postAction = postActionRepository.findByPostActionIdAndCommunity(postId,
                communityCommonService.getCommunity(communityId));
        return this.getPostBaseDTOFromPostId(postAction, userWhoShared, postActionType);
    }

    public PostBaseDTO getPostBaseDTOFromPostId(Long postId) {
        return this.getPostBaseDTOFromPostId(postId, null, PostActionType.normal);
    }

    public PostBaseDTO getPostBaseDTOFromPostId(PostAction postAction) {
        return this.getPostBaseDTOFromPostId(postAction, null, PostActionType.normal);
    }

    public PostBaseDTO getPostBaseDTOFromPostId(String postIdString) {
        try {
            return this.getPostBaseDTOFromPostId(Long.parseLong(postIdString), null, PostActionType.normal);
        } catch (Exception e) {
            return null;
        }
    }

    public PostBaseDTO getPostBaseDTOFromPostId(Long communityId, String postIdString) {
        try {
            return this.getPostBaseDTOFromPostId(Long.parseLong(postIdString),
                    null,
                    PostActionType.normal,
                    communityId);
        } catch (Exception e) {
            return null;
        }
    }

    public Page<PostBaseDTO> getPostBaseDTOPageFromPostId(List<String> postIdStringList,
                                                          Pageable pageable,
                                                          Long communityId,
                                                          Integer totalElements) {
        List<PostBaseDTO> postBaseDTOArrayList = new ArrayList<>();
        for (String postIdString : postIdStringList) {
            PostBaseDTO postBaseDTO;
            if (communityId != null)
                postBaseDTO = this.getPostBaseDTOFromPostId(communityId, postIdString);
            else
                postBaseDTO = this.getPostBaseDTOFromPostId(postIdString);
            if (postBaseDTO != null) {
                postBaseDTOArrayList.add(postBaseDTO);
            }
        }
        // List sorted with descending order of post creation date
        Comparator<PostBaseDTO> postBaseDTOComparator
                = Comparator.comparing((
                postBaseDTO -> postBaseDTO.getMetaData().getCreatedAtTimestamp()
        ));
        postBaseDTOArrayList.sort(
                postBaseDTOComparator.reversed()
        );
        return new PageImpl<>(postBaseDTOArrayList, pageable, totalElements != null ?
                totalElements :
                postBaseDTOArrayList.size());
    }

    public Page<PostBaseDTO> getPostBaseDTOPageFromPostId(List<String> postIdStringList,
                                                          Pageable pageable,
                                                          Long communityId) {
        return this.getPostBaseDTOPageFromPostId(postIdStringList, pageable, communityId, null);
    }

    public Page<PostBaseDTO> getPostBaseDTOPageFromPostId(List<String> postIdStringList,
                                                          Pageable pageable,
                                                          Integer totalElements) {
        return this.getPostBaseDTOPageFromPostId(postIdStringList, pageable, null, totalElements);
    }

    public Page<PostBaseDTO> getPostBaseDTOPageFromPostId(List<String> postIdStringList,
                                                          Pageable pageable) {
        return this.getPostBaseDTOPageFromPostId(postIdStringList, pageable, null, null);
    }
}
