package com.questnr.model.mapper;

import com.questnr.model.dto.UserDTO;
import com.questnr.model.entities.User;
import com.questnr.model.projections.UserProjection;
import com.questnr.services.user.UserCommonService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = {AvatarMapper.class, UserMetaMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    UserCommonService userCommonService;

    @Mappings({
            @Mapping(source = "avatar", target = "avatarDTO", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT),
            @Mapping(target = "userMeta", expression = "java(UserMetaMapper.getMetaMapper(user, this.userCommonService))")
    })
    abstract public UserDTO toOthersDTO(final User user);

    public List<UserDTO> toOthersDTOs(final List<User> users) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(this.toOthersDTO(user));
        }
        return userDTOS;
    }

    abstract public UserDTO toOthersDTOsFromProjection(final UserProjection user);

    public List<UserDTO> toOthersDTOsFromProjections(final List<UserProjection> users) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (UserProjection user : users) {
            userDTOS.add(this.toOthersDTOsFromProjection(user));
        }
        return userDTOS;
    }
}
