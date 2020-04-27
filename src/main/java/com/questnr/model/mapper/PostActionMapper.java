package com.questnr.model.mapper;

import com.questnr.model.dto.*;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import org.mapstruct.*;

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

    @Mappings({
            @Mapping(source = "postAction.slug", target = "slug"),
            @Mapping(source = "postAction.postMediaList", target = "postMediaDTOList"),
            @Mapping(source = "postAction.community", target = "communityDTO"),
            @Mapping(source = "postAction.userActor", target = "userDTO"),
            @Mapping(source = "postAction.commentActionSet", target = "commentActionDTOList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "metaList", ignore = true),
            @Mapping(target = "postActionMeta", expression = "java(PostActionMetaMapper.getMetaMapper(postAction, user))")
    })
    abstract public PostActionDTO toDTO(final PostAction postAction, final User user);

    public List<PostActionDTO> toDTOs(final List<PostAction> postActionList, final User user) {
        List<PostActionDTO> postActionDTOS = new ArrayList<>();
        for (PostAction postAction : postActionList) {
            postActionDTOS.add(this.toDTO(postAction, user));
        }
        return postActionDTOS;
    }

    @Mappings({
            @Mapping(source = "postMediaList", target = "postMediaDTOList"),
            @Mapping(source = "community", target = "communityDTO"),
            @Mapping(source = "userActor", target = "userDTO"),
            @Mapping(source = "commentActionSet", target = "commentActionDTOList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "metaList", ignore = true)
    })
    abstract public PostActionPublicDTO toPublicDTO(final PostAction postAction);

    public List<PostActionPublicDTO> toPublicDTOs(final List<PostAction> postActionList) {
        List<PostActionPublicDTO> postActionDTOS = new ArrayList<>();
        for (PostAction postAction : postActionList) {
            postActionDTOS.add(this.toPublicDTO(postAction));
        }
        return postActionDTOS;
    }


    abstract public PostAction fromPostActionRequestDTO(final PostActionRequestDTO postActionRequestDTO);

    @Mappings({
            @Mapping(source = "postMediaList", target = "postMediaDTOList"),
            @Mapping(source = "userActor", target = "userDTO"),
            @Mapping(source = "commentActionSet", target = "commentActionDTOList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))")
    })
    abstract public PostActionForCommunityDTO toPostActionForCommunityDTO(final PostAction postAction);

    @Mappings({
            @Mapping(source = "postMediaList", target = "postMediaDTOList"),
            @Mapping(source = "community", target = "communityDTO"),
            @Mapping(source = "userActor", target = "userDTO")
    })
    abstract public PostActionForMediaDTO toPostActionForMediaDTO(final PostAction postAction);
}
