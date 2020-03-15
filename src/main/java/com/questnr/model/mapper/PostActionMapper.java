package com.questnr.model.mapper;

import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.entities.PostAction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(uses = { PostMediaMapper.class }, unmappedTargetPolicy =  ReportingPolicy.IGNORE, componentModel =  "spring")
public interface PostActionMapper {

    @Mapping(source = "postMediaList", target = "postMediaDTOList")
    PostActionDTO toDTO(final PostAction postAction);

    PostAction toDomain(final PostActionDTO postActionDTO);

    List<PostActionDTO> toDTOs(final List<PostAction> postActions);
}
