package com.questnr.model.mapper;

import com.questnr.model.dto.PostActionForCommunityDTO;
import com.questnr.model.dto.PostActionRequestDTO;
import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.entities.PostAction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(uses = {PostMediaMapper.class, CommunityMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PostActionMapper {

    @Mappings({
            @Mapping(source = "postMediaList", target = "postMediaDTOList"),
            @Mapping(source = "community", target = "communityDTO")
    })
    PostActionDTO toDTO(final PostAction postAction);

    PostAction fromPostActionRequestDTO(final PostActionRequestDTO postActionRequestDTO);

    List<PostActionDTO> toDTOs(final List<PostAction> postActions);

    PostActionForCommunityDTO toPostActionForCommunityDTO(final PostAction postAction);

    List<PostActionForCommunityDTO> toPostActionForCommunityDTOs(final List<PostAction> postActions);
}
