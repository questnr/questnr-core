package com.questnr.model.mapper;

import com.questnr.model.dto.ChildCommentActionDTO;
import com.questnr.model.dto.CommentActionDTO;
import com.questnr.model.entities.CommentAction;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = {UserMapper.class, MetaDataMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class CommentActionMapper {

    @Mappings({
            @Mapping(source = "userActor", target = "userActorDTO"),
            @Mapping(source = "childCommentSet", target = "childCommentDTOSet", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL),
            @Mapping(source = "createdAt", target = "metaData")
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
            @Mapping(source = "createdAt", target = "metaData")
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
