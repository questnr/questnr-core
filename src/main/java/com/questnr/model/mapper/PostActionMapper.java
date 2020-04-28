package com.questnr.model.mapper;

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
        CommentActionMapper.class,
        MetaDataMapper.class,
        PostActionMetaMapper.class
}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class PostActionMapper {

    @Autowired
    UserCommonService userCommonService;

    @Mappings({
            @Mapping(source = "slug", target = "slug"),
            @Mapping(source = "postMediaList", target = "postMediaList"),
            @Mapping(source = "community", target = "communityDTO"),
            @Mapping(source = "userActor", target = "userDTO"),
            @Mapping(source = "commentActionSet", target = "commentActionDTOList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "metaList", ignore = true),
            @Mapping(target = "postActionMeta", expression = "java(PostActionMetaMapper.getMetaMapper(postAction, this.userCommonService))")
    })
    abstract public PostActionDTO toDTO(final PostAction postAction);

    public List<PostActionDTO> toDTOs(final List<PostAction> postActionList) {
        List<PostActionDTO> postActionDTOS = new ArrayList<>();
        for (PostAction postAction : postActionList) {
            postActionDTOS.add(this.toDTO(postAction));
        }
        return postActionDTOS;
    }

    @Mappings({
            @Mapping(source = "slug", target = "slug"),
            @Mapping(source = "postMediaList", target = "postMediaList"),
            @Mapping(source = "community", target = "communityDTO"),
            @Mapping(source = "commentActionSet", target = "commentActionDTOList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "postActionMeta", expression = "java(PostActionMetaMapper.getMetaMapper(postAction, this.userCommonService))")
    })
    abstract public PostActionCardDTO toCardDTO(final PostAction postAction);

    public List<PostActionCardDTO> toCardDTOs(final List<PostAction> postActionList) {
        List<PostActionCardDTO> postActionCardDTOS = new ArrayList<>();
        for (PostAction postAction : postActionList) {
            postActionCardDTOS.add(this.toCardDTO(postAction));
        }
        return postActionCardDTOS;
    }

    abstract public PostAction fromPostActionRequestDTO(final PostActionRequestDTO postActionRequestDTO);

    @Mappings({
            @Mapping(source = "postMediaList", target = "postMediaList"),
            @Mapping(source = "userActor", target = "userDTO"),
            @Mapping(source = "commentActionSet", target = "commentActionDTOList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))")
    })
    abstract public PostActionForCommunityDTO toPostActionForCommunityDTO(final PostAction postAction);

    @Mappings({
            @Mapping(source = "postMediaList", target = "postMediaList"),
            @Mapping(source = "community", target = "communityDTO"),
            @Mapping(source = "userActor", target = "userDTO")
    })
    abstract public PostActionForMediaDTO toPostActionForMediaDTO(final PostAction postAction);
}
