package com.questnr.model.mapper;

import com.questnr.model.dto.LikeActionDTO;
import com.questnr.model.entities.LikeAction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(uses = {UserMapper.class, MetaDataMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class LikeActionMapper {

    @Mappings({
            @Mapping(source = "userActor", target = "user"),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(likeAction.getCreatedAt(), likeAction.getUpdatedAt()))")
    })
    public abstract LikeActionDTO toDTO(final LikeAction likeAction);

    public abstract List<LikeActionDTO> toDTOs(final List<LikeAction> likeActionList);
}
