package com.questnr.model.mapper;

import com.questnr.model.dto.UserWithRankDTO;
import com.questnr.model.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = {AvatarMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class UserWithRankMapper {

    @Mapping(source = "avatar", target = "avatarDTO")
    abstract public UserWithRankDTO toUserWithRankDTO(User user);
}
