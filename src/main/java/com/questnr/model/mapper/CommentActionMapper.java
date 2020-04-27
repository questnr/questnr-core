package com.questnr.model.mapper;

import com.questnr.model.dto.ChildCommentActionDTO;
import com.questnr.model.dto.CommentActionDTO;
import com.questnr.model.entities.CommentAction;
import com.questnr.services.user.UserCommonService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = {UserMapper.class, MetaDataMapper.class, CommentActionMetaMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class CommentActionMapper {

    @Autowired
    UserCommonService userCommonService;

    @Mappings({
            @Mapping(source = "userActor", target = "userActorDTO"),
            @Mapping(source = "childCommentSet", target = "childCommentDTOSet", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(commentAction.getCreatedAt(), commentAction.getUpdatedAt()))"),
            @Mapping(target = "commentActionMeta", expression = "java(CommentActionMetaMapper.getMetaMapper(commentAction, this.userCommonService))")
    })
    abstract public CommentActionDTO toDTO(final CommentAction commentAction);

    public List<CommentActionDTO> toDTOs(final List<CommentAction> commentActions) {
        List<CommentActionDTO> commentActionDTOS = new ArrayList<>();
        for (CommentAction commentAction : commentActions) {
            commentActionDTOS.add(this.toDTO(commentAction));
        }
        return commentActionDTOS;
    }

    @Mappings({
            @Mapping(source = "userActor", target = "userActorDTO"),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(commentAction.getCreatedAt(), commentAction.getUpdatedAt()))"),
            @Mapping(target = "commentActionMeta", expression = "java(CommentActionMetaMapper.getMetaMapper(commentAction, this.userCommonService))")
    })
    abstract public ChildCommentActionDTO toChildCommentActionDTO(final CommentAction commentAction);

    public List<ChildCommentActionDTO> toChildCommentActionDTOs(final List<CommentAction> commentActions) {
        List<ChildCommentActionDTO> childCommentActionDTOS = new ArrayList<>();
        for (CommentAction commentAction : commentActions) {
            childCommentActionDTOS.add(this.toChildCommentActionDTO(commentAction));
        }
        return childCommentActionDTOS;
    }
}
