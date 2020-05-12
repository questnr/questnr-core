package com.questnr.model.mapper;

import com.questnr.common.enums.PostActionType;
import com.questnr.model.dto.*;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.CommentActionRepository;
import com.questnr.services.user.UserCommonService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = {
        PostMediaMapper.class,
        CommunityMapper.class,
        UserMapper.class,
        LikeActionDefaultMapper.class,
        CommentActionDefaultMapper.class,
        MetaDataMapper.class,
        PostActionMetaMapper.class
}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class PostActionMapper {

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    CommentActionRepository commentActionRepository;

    @Mappings({
            @Mapping(source = "slug", target = "slug"),
            @Mapping(source = "postMediaList", target = "postMediaList"),
            @Mapping(source = "community", target = "communityDTO"),
            @Mapping(source = "userActor", target = "userDTO"),
            @Mapping(source = "likeActionSet", target = "likeActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(source = "commentActionSet", target = "commentActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "metaList", ignore = true),
            @Mapping(target = "postActionMeta", expression = "java(PostActionMetaMapper.getMetaMapper(postAction, this.userCommonService))"),
            @Mapping(target = "totalLikes", expression = "java(postAction.getLikeActionSet().size())"),
            @Mapping(target = "totalComments", expression = "java(this.commentActionRepository.countByPostActionAndChildComment(postAction, false))"),
            @Mapping(target = "totalPostVisits", expression = "java(postAction.getPostVisitSet().size())")
    })
    abstract public PostActionPublicDTO toPublicDTO(final PostAction postAction);

    public List<PostActionPublicDTO> toPublicDTOs(final List<PostAction> postActionList) {
        List<PostActionPublicDTO> postActionDTOS = new ArrayList<>();
        for (PostAction postAction : postActionList) {
            postActionDTOS.add(this.toPublicDTO(postAction));
        }
        return postActionDTOS;
    }

    @Mappings({
            @Mapping(source = "postAction.slug", target = "slug"),
            @Mapping(source = "postAction.postMediaList", target = "postMediaList"),
            @Mapping(source = "postAction.community", target = "communityDTO"),
            @Mapping(source = "postAction.likeActionSet", target = "likeActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(source = "postAction.commentActionSet", target = "commentActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "postActionMeta", expression = "java(PostActionMetaMapper.getMetaMapper(postAction, this.userCommonService))"),
            @Mapping(target = "totalLikes", expression = "java(postAction.getLikeActionSet().size())"),
            @Mapping(target = "totalComments", expression = "java(postAction.getCommentActionSet().size())"),
            @Mapping(target = "totalPostVisits", expression = "java(postAction.getPostVisitSet().size())"),
            @Mapping(target = "postActionType", expression = "java(postActionType)"),
            @Mapping(source = "sharedPostOwner", target = "sharedPostOwner")
    })
    abstract public PostActionCardDTO toCardDTO(final PostAction postAction, PostActionType postActionType, User sharedPostOwner);

    @Mappings({
            @Mapping(source = "postAction.slug", target = "slug"),
            @Mapping(source = "postAction.postMediaList", target = "postMediaList"),
            @Mapping(source = "postAction.userActor", target = "userDTO"),
            @Mapping(source = "postAction.community", target = "communityDTO"),
            @Mapping(source = "postAction.likeActionSet", target = "likeActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(source = "postAction.commentActionSet", target = "commentActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "postActionMeta", expression = "java(PostActionMetaMapper.getMetaMapper(postAction, this.userCommonService))"),
            @Mapping(target = "totalLikes", expression = "java(postAction.getLikeActionSet().size())"),
            @Mapping(target = "totalComments", expression = "java(this.commentActionRepository.countByPostActionAndChildComment(postAction, false))"),
            @Mapping(target = "totalPostVisits", expression = "java(postAction.getPostVisitSet().size())"),
            @Mapping(target = "postActionType", expression = "java(postActionType)"),
            @Mapping(source = "userWhoShared", target = "userWhoShared")
    })
    abstract public PostActionFeedDTO toFeedDTO(final PostAction postAction, PostActionType postActionType, User userWhoShared);

    abstract public PostAction fromPostActionRequestDTO(final PostActionRequestDTO postActionRequestDTO);

    @Mappings({
            @Mapping(source = "postMediaList", target = "postMediaList"),
            @Mapping(source = "userActor", target = "userDTO"),
            @Mapping(source = "likeActionSet", target = "likeActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(source = "commentActionSet", target = "commentActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "totalLikes", expression = "java(postAction.getLikeActionSet().size())"),
            @Mapping(target = "totalComments", expression = "java(this.commentActionRepository.countByPostActionAndChildComment(postAction, false))"),
            @Mapping(target = "totalPostVisits", expression = "java(postAction.getPostVisitSet().size())"),
            @Mapping(target = "postActionMeta", expression = "java(PostActionMetaMapper.getMetaMapper(postAction, this.userCommonService))"),
    })
    abstract public PostActionForCommunityDTO toPostActionForCommunityDTO(final PostAction postAction);

    @Mappings({
            @Mapping(source = "postMediaList", target = "postMediaList"),
            @Mapping(source = "community", target = "communityDTO"),
            @Mapping(source = "userActor", target = "userDTO")
    })
    abstract public PostActionForMediaDTO toPostActionForMediaDTO(final PostAction postAction);
}
