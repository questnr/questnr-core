package com.questnr.model.mapper;

import com.questnr.common.enums.PostActionType;
import com.questnr.common.enums.PostType;
import com.questnr.model.dto.*;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
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
        PostActionMetaMapper.class,
        PostPollQuestionMapper.class
}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class PostActionMapper {

    @Autowired
    UserCommonService userCommonService;

    @Mappings({
            @Mapping(source = "postMediaList", target = "postMediaList"),
            @Mapping(source = "community", target = "communityDTO"),
            @Mapping(source = "userActor", target = "userDTO"),
            @Mapping(source = "likeActionSet", target = "likeActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(source = "commentActionSet", target = "commentActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "metaList", ignore = true),
            @Mapping(target = "postActionMeta", expression = "java(PostActionMetaMapper.getMetaMapper(postAction, this.userCommonService))"),
            @Mapping(target = "totalLikes", expression = "java(postAction.getLikeActionSet().size())"),
            @Mapping(target = "totalComments", expression = "java(postAction.getCommentActionSet().size())"),
            @Mapping(target = "totalPostVisits", expression = "java(postAction.getPostVisitSet().size())")
    })
    abstract public PostActionPublicDTO toPublicDTO(final PostAction postAction);

    @Mappings({
            @Mapping(source = "community", target = "communityDTO"),
            @Mapping(source = "userActor", target = "userDTO"),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "metaList", ignore = true),
            @Mapping(target = "postActionMeta", expression = "java(PostActionMetaMapper.getMetaMapper(postAction, this.userCommonService))")
    })
    abstract public PostPollQuestionPublicDTO toPollQuestionPublicDTO(final PostAction postAction);

    public List<PostBaseDTO> toPublicDTOs(final List<PostAction> postActionList) {
        List<PostBaseDTO> postActionDTOS = new ArrayList<>();
        for (PostAction postAction : postActionList) {
            if (postAction.getPostType() == PostType.simple) {
                postActionDTOS.add(this.toPublicDTO(postAction));
            } else {
                postActionDTOS.add(this.toPollQuestionPublicDTO(postAction));
            }
        }
        return postActionDTOS;
    }

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
            @Mapping(target = "totalComments", expression = "java(postAction.getCommentActionSet().size())"),
            @Mapping(target = "totalPostVisits", expression = "java(postAction.getPostVisitSet().size())"),
            @Mapping(target = "postActionType", expression = "java(postActionType)"),
            @Mapping(source = "userWhoShared", target = "userWhoShared")
    })
    abstract public PostActionFeedDTO toPostActionFeedDTO(final PostAction postAction, PostActionType postActionType, User userWhoShared);

    @Mappings({
            @Mapping(source = "postAction.slug", target = "slug"),
            @Mapping(source = "postAction.userActor", target = "userDTO"),
            @Mapping(source = "postAction.community", target = "communityDTO"),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "postActionMeta", expression = "java(PostActionMetaMapper.getMetaMapper(postAction, this.userCommonService))"),
            @Mapping(target = "postActionType", expression = "java(postActionType)")
    })
    abstract public PostPollQuestionFeedDTO toPostPollQuestionFeedDTO(final PostAction postAction, PostActionType postActionType, User userWhoShared);

    abstract public PostAction fromPostActionRequestDTO(final PostActionRequestDTO postActionRequestDTO);

    @Mappings({
            @Mapping(source = "postMediaList", target = "postMediaList"),
            @Mapping(source = "userActor", target = "userDTO"),
            @Mapping(source = "likeActionSet", target = "likeActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(source = "commentActionSet", target = "commentActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "totalLikes", expression = "java(postAction.getLikeActionSet().size())"),
            @Mapping(target = "totalComments", expression = "java(postAction.getCommentActionSet().size())"),
            @Mapping(target = "totalPostVisits", expression = "java(postAction.getPostVisitSet().size())"),
            @Mapping(target = "postActionMeta", expression = "java(PostActionMetaMapper.getMetaMapper(postAction, this.userCommonService))"),
    })
    abstract public PostActionForCommunityDTO toPostActionForCommunityDTO(final PostAction postAction);

    @Mappings({
            @Mapping(source = "userActor", target = "userDTO"),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "postActionMeta", expression = "java(PostActionMetaMapper.getMetaMapper(postAction, this.userCommonService))")
    })
    abstract public PostPollQuestionForCommunityDTO toPostPollQuestionForCommunityDTO(final PostAction postAction);

    @Mappings({
            @Mapping(source = "postMediaList", target = "postMediaList"),
            @Mapping(source = "community", target = "communityDTO"),
            @Mapping(source = "userActor", target = "userDTO")
    })
    abstract public PostActionForMediaDTO toPostActionForMediaDTO(final PostAction postAction);
}
