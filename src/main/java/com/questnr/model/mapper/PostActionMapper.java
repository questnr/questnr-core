package com.questnr.model.mapper;

import com.questnr.common.enums.PostActionType;
import com.questnr.common.enums.PostType;
import com.questnr.model.dto.post.PostBaseDTO;
import com.questnr.model.dto.post.normal.*;
import com.questnr.model.dto.post.question.PostPollQuestionFeedDTO;
import com.questnr.model.dto.post.question.PostPollQuestionForCommunityDTO;
import com.questnr.model.dto.post.question.PostPollQuestionPublicDTO;
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
        PostPollQuestionMapper.class,
        PostPollQuestionMetaMapper.class,
        NormalPostMapper.class
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
            @Mapping(target = "postData", expression = "java(NormalPostMapper.getMetaMapper(postAction, false))")
    })
    abstract public PostActionPublicDTO toPublicDTO(final PostAction postAction);

    public PostBaseDTO toPostBasePublicDTO(final PostAction postAction) {
        return postAction.getPostType() == PostType.simple ?
                this.toPublicDTO(postAction) :
                this.toPollQuestionPublicDTO(postAction);
    }

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
            @Mapping(source = "postMediaList", target = "postMediaList"),
            @Mapping(source = "community", target = "communityDTO"),
            @Mapping(source = "userActor", target = "userDTO"),
            @Mapping(source = "likeActionSet", target = "likeActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(source = "commentActionSet", target = "commentActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "metaList", ignore = true),
            @Mapping(target = "postActionMeta", expression = "java(PostActionMetaMapper.getMetaMapper(postAction, this.userCommonService))"),
            @Mapping(target = "postData", expression = "java(NormalPostMapper.getMetaMapper(postAction, true))")
    })
    abstract public PostActionPublicDTO toSinglePostPublicDTO(final PostAction postAction);

    @Mappings({
            @Mapping(source = "postAction.slug", target = "slug"),
            @Mapping(source = "postAction.postMediaList", target = "postMediaList"),
            @Mapping(source = "postAction.userActor", target = "userDTO"),
            @Mapping(source = "postAction.community", target = "communityDTO"),
            @Mapping(source = "postAction.likeActionSet", target = "likeActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(source = "postAction.commentActionSet", target = "commentActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "postActionMeta", expression = "java(PostActionMetaMapper.getMetaMapper(postAction, this.userCommonService))"),
            @Mapping(target = "postActionType", expression = "java(postActionType)"),
            @Mapping(source = "userWhoShared", target = "userWhoShared"),
            @Mapping(target = "postData", expression = "java(NormalPostMapper.getMetaMapper(postAction, false))")
    })
    abstract public PostActionFeedDTO toPostActionFeedDTO(final PostAction postAction, PostActionType postActionType, User userWhoShared);

    @Mappings({
            @Mapping(source = "postAction.slug", target = "slug"),
            @Mapping(source = "postAction.userActor", target = "userDTO"),
            @Mapping(source = "postAction.community", target = "communityDTO"),
            @Mapping(source = "postAction.postPollQuestion", target = "pollQuestion"),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "pollQuestionMeta", expression = "java(PostPollQuestionMetaMapper.getMetaMapper(postAction, this.userCommonService))"),
            @Mapping(target = "postActionType", expression = "java(postActionType)"),
            @Mapping(source = "postAction.text", target = "questionText")
    })
    abstract public PostPollQuestionFeedDTO toPostPollQuestionFeedDTO(final PostAction postAction, PostActionType postActionType, User userWhoShared);

    abstract public PostAction fromPostActionRequestDTO(final PostActionRequestDTO postActionRequestDTO);

    @Mappings({
            @Mapping(source = "postMediaList", target = "postMediaList"),
            @Mapping(source = "userActor", target = "userDTO"),
            @Mapping(source = "likeActionSet", target = "likeActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(source = "commentActionSet", target = "commentActionList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "postActionMeta", expression = "java(PostActionMetaMapper.getMetaMapper(postAction, this.userCommonService))"),
            @Mapping(target = "postData", expression = "java(NormalPostMapper.getMetaMapper(postAction, false))")
    })
    abstract public PostActionForCommunityDTO toPostActionForCommunityDTO(final PostAction postAction);

    @Mappings({
            @Mapping(source = "community", target = "communityDTO"),
            @Mapping(source = "userActor", target = "userDTO"),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "metaList", ignore = true),
            @Mapping(target = "pollQuestionMeta", expression = "java(PostPollQuestionMetaMapper.getMetaMapper(postAction, this.userCommonService))"),
            @Mapping(source = "postPollQuestion", target = "pollQuestion"),
            @Mapping(source = "text", target = "questionText")
    })
    abstract public PostPollQuestionPublicDTO toPollQuestionPublicDTO(final PostAction postAction);

    @Mappings({
            @Mapping(source = "userActor", target = "userDTO"),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "pollQuestionMeta", expression = "java(PostPollQuestionMetaMapper.getMetaMapper(postAction, this.userCommonService))"),
            @Mapping(source = "postPollQuestion", target = "pollQuestion"),
            @Mapping(source = "text", target = "questionText")
    })
    abstract public PostPollQuestionForCommunityDTO toPostPollQuestionForCommunityDTO(final PostAction postAction);

    @Mappings({
            @Mapping(source = "postMediaList", target = "postMediaList"),
            @Mapping(source = "community", target = "communityDTO"),
            @Mapping(source = "userActor", target = "userDTO")
    })
    abstract public PostActionForMediaDTO toPostActionForMediaDTO(final PostAction postAction);
}
