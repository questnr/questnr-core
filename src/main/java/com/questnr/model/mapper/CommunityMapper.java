package com.questnr.model.mapper;

import com.questnr.model.dto.*;
import com.questnr.model.entities.Community;
import com.questnr.services.user.UserCommonService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = {UserMapper.class, AvatarMapper.class, MetaDataMapper.class, CommunityMetaMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class CommunityMapper {

    @Autowired
    UserCommonService userCommonService;

    @Mappings({
            @Mapping(source = "ownerUser", target = "ownerUserDTO"),
            @Mapping(source = "avatar", target = "avatarDTO", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT),
            @Mapping(target = "totalMembers", expression = "java(community.getUsers().size())"),
            @Mapping(target = "metaData", expression = "java(MetaDataMapper.getMetaDataMapper(community.getCreatedAt(), community.getUpdatedAt()))"),
            @Mapping(target = "metaList", ignore = true),
            @Mapping(target = "communityMeta", expression = "java(CommunityMetaMapper.getMetaMapper(community, this.userCommonService))")
    })
    public abstract CommunityDTO toDTO(final Community community);

    public List<CommunityDTO> toDTOs(final List<Community> communities) {
        List<CommunityDTO> communityDTOList = new ArrayList<>();
        for (Community community : communities) {
            communityDTOList.add(this.toDTO(community));
        }
        return communityDTOList;
    }

    @Mapping(target = "avatar", ignore = true)
    public abstract Community toDomain(CommunityRequestDTO communityRequestDTO);

    @Mapping(source = "avatar", target = "avatarDTO", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    public abstract CommunityForPostActionDTO toCommunityForPostAction(Community community);

    @Mappings({
            @Mapping(source = "ownerUser", target = "ownerUserDTO"),
            @Mapping(source = "avatar", target = "avatarDTO", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT),
            @Mapping(target = "totalMembers", expression = "java(community.getUsers().size())")
    })
    public abstract CommunityCardDTO toCommunityCard(Community community);

    public abstract List<CommunityCardDTO> toCommunityCards(List<Community> communityList);
}
