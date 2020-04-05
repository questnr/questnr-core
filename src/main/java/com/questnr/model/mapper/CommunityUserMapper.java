package com.questnr.model.mapper;

import com.questnr.model.dto.CommunityUserForCommunityDTO;
import com.questnr.model.entities.CommunityUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = {UserMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class CommunityUserMapper {

    @Mapping(source = "user", target = "communityUser")
    abstract public CommunityUserForCommunityDTO toDTOForCommunity(final CommunityUser communityUser);

    public List<CommunityUserForCommunityDTO> toDTOsForCommunity(final List<CommunityUser> communityUserList){
        List<CommunityUserForCommunityDTO> communityUserForCommunityDTOS = new ArrayList<>();
        for(CommunityUser communityUser: communityUserList){
            communityUserForCommunityDTOS.add(this.toDTOForCommunity(communityUser));
        }
        return communityUserForCommunityDTOS;
    }
}
