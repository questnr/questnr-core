package com.questnr.model.mapper;

import com.questnr.model.dto.TrendingPostDTO;
import com.questnr.model.entities.PostAction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = { PostMediaMapper.class }, unmappedTargetPolicy =  ReportingPolicy.IGNORE, componentModel =  "spring")
public interface TrendingPostMapper {

    @Mapping(source = "postMediaList", target = "postMediaDTOList")
    TrendingPostDTO toDTO(PostAction postAction);

    PostAction toDomain(TrendingPostDTO trendingPostDTO);
}
