package com.questnr.model.mapper;

import com.questnr.model.dto.UserDTO;
import com.questnr.model.entities.User;
import com.questnr.model.projections.UserProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(uses = {AvatarMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "avatar", target = "avatarDTO", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    UserDTO toOthersDTO(final User user);

    List<UserDTO> toOthersDTOs(final List<User> users);

    List<UserDTO> toOthersDTOsFromProjections(final List<UserProjection> users);
}
