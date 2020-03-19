package com.questnr.model.mapper;

import com.questnr.model.dto.CommunityDTO;
import com.questnr.model.entities.Community;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(uses = {UserMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CommunityMapper {

    @Mapping(source = "ownerUser", target = "ownerUserDTO")
    CommunityDTO toDTO(final Community community);

    Community toDomain(final CommunityDTO communityDTO);

    List<CommunityDTO> toDTOs(final List<Community> communities);
}
