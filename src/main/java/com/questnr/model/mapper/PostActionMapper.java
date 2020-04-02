package com.questnr.model.mapper;

import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.dto.PostActionForCommunityDTO;
import com.questnr.model.dto.PostActionRequestDTO;
import com.questnr.model.entities.PostAction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = {PostMediaMapper.class, CommunityMapper.class, UserMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PostActionMapper {

    @Mappings({
            @Mapping(source = "postMediaList", target = "postMediaDTOList"),
            @Mapping(source = "community", target = "communityDTO"),
            @Mapping(source = "userActor", target = "userDTO")
    })
    PostActionDTO toDTO(final PostAction postAction);

    PostAction fromPostActionRequestDTO(final PostActionRequestDTO postActionRequestDTO);

    @Mapping(source = "postMediaList", target = "postMediaDTOList")
    PostActionForCommunityDTO toPostActionForCommunityDTO(final PostAction postAction);
}
