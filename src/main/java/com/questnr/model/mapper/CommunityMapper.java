package com.questnr.model.mapper;

import com.questnr.model.dto.CommunityDTO;
import com.questnr.model.entities.Community;
import org.mapstruct.*;

import java.util.List;

@Mapper(uses = {UserMapper.class, AvatarMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CommunityMapper {

    @Mappings({
            @Mapping(source = "ownerUser", target = "ownerUserDTO"),
            @Mapping(source = "avatar", target = "avatarDTO", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT),
    })
    CommunityDTO toDTO(final Community community);

    Community toDomain(final CommunityDTO communityDTO);

    List<CommunityDTO> toDTOs(final List<Community> communities);
}
