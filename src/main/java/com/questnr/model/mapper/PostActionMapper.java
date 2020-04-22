package com.questnr.model.mapper;

import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.dto.PostActionForCommunityDTO;
import com.questnr.model.dto.PostActionForMediaDTO;
import com.questnr.model.dto.PostActionRequestDTO;
import com.questnr.model.entities.PostAction;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = {
        PostMediaMapper.class,
        CommunityMapper.class,
        UserMapper.class,
        CommentActionMapper.class,
        MetaDataMapper.class
}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class PostActionMapper {

    @Mappings({
            @Mapping(source = "postMediaList", target = "postMediaDTOList"),
            @Mapping(source = "community", target = "communityDTO"),
            @Mapping(source = "userActor", target = "userDTO"),
            @Mapping(source = "commentActionSet", target = "commentActionDTOList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(postAction.getCreatedAt(), postAction.getUpdatedAt()))"),
            @Mapping(target = "metaList", ignore = true)
    })
    abstract public PostActionDTO toDTO(final PostAction postAction);

    public List<PostActionDTO> toDTOs(final List<PostAction> postActionList) {
        List<PostActionDTO> postActionDTOS = new ArrayList<>();
        for (PostAction postAction : postActionList) {
            postActionDTOS.add(this.toDTO(postAction));
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
