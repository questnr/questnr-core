package com.questnr.model.mapper;

import com.questnr.model.dto.CommunityDTO;
import com.questnr.model.dto.CommunityForPostActionDTO;
import com.questnr.model.dto.CommunityRequestDTO;
import com.questnr.model.entities.Community;
import org.mapstruct.*;

import java.util.List;

@Mapper(uses = {UserMapper.class, AvatarMapper.class, CommunityUserMapper.class, MetaDataMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CommunityMapper {

    @Mappings({
            @Mapping(source = "ownerUser", target = "ownerUserDTO"),
            @Mapping(source = "avatar", target = "avatarDTO", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT),
            @Mapping(source = "users", target = "communityUsers"),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(community.getCreatedAt(), community.getUpdatedAt()))")
    })
    CommunityDTO toDTO(final Community community);

    List<CommunityDTO> toDTOs(final List<Community> communities);

    @Mapping(target = "avatar", ignore = true)
    Community toDomain(CommunityRequestDTO communityRequestDTO);

    @Mapping(source = "avatar", target = "avatarDTO", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    CommunityForPostActionDTO toCommunityForPostAction(Community community);
}
