package com.questnr.model.mapper;

import com.questnr.model.dto.LikeCommentActionDTO;
import com.questnr.model.entities.LikeCommentAction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(uses = {UserMapper.class, MetaDataMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class LikeCommentActionMapper {

    @Mappings({
            @Mapping(source = "userActor", target = "user"),
            @Mapping(source = "createdAt", target = "metaData")
    })
    public abstract LikeCommentActionDTO toDTO(final LikeCommentAction likeAction);

    public abstract List<LikeCommentActionDTO> toDTOs(final List<LikeCommentAction> likeActionList);
}
