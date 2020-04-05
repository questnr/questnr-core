package com.questnr.model.mapper;

import com.questnr.model.dto.UserDTO;
import com.questnr.model.dto.UserWithRankDTO;
import com.questnr.model.entities.CommunityUser;
import com.questnr.model.entities.User;
import com.questnr.model.projections.UserProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = {AvatarMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class UserMapper {

    @Mapping(source = "avatar", target = "avatarDTO", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    abstract public UserDTO toOthersDTO(final User user);

    public List<UserDTO> toOthersDTOs(final List<User> users){
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User user: users){
            userDTOS.add(this.toOthersDTO(user));
        }
        return userDTOS;
    }

    abstract public UserDTO toOthersDTOsFromProjection(final UserProjection user);

    public List<UserDTO> toOthersDTOsFromProjections(final List<UserProjection> users){
        List<UserDTO> userDTOS = new ArrayList<>();
        for(UserProjection user: users){
            userDTOS.add(this.toOthersDTOsFromProjection(user));
        }
        return userDTOS;
    }
}
