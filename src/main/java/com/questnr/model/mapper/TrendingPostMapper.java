package com.questnr.model.mapper;

import com.questnr.model.dto.TrendingPostDTO;
import com.questnr.model.entities.PostAction;
import org.mapstruct.*;

@Mapper(uses = {PostMediaMapper.class, UserMapper.class, CommunityMapper.class, CommentActionMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TrendingPostMapper {

    @Mappings({
            @Mapping(source = "postMediaList", target = "postMediaDTOList"),
            @Mapping(source = "community", target = "communityDTO"),
            @Mapping(source = "userActor", target = "userDTO"),
            @Mapping(source = "commentActionSet", target = "commentActionDTOList", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    })
    TrendingPostDTO toDTO(PostAction postAction);
}
