package com.questnr.model.mapper;

import com.questnr.model.dto.BioUserDTO;
import com.questnr.model.dto.user.UserDTO;
import com.questnr.model.dto.user.UserOtherDTO;
import com.questnr.model.dto.user.UserProfileDTO;
import com.questnr.model.entities.User;
import com.questnr.model.projections.UserProjection;
import com.questnr.requests.UserRequest;
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
    abstract public UserOtherDTO toOthersDTO(final User user);

    public List<UserOtherDTO> toOthersDTOs(final List<User> users) {
        List<UserOtherDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(this.toOthersDTO(user));
        }
        return userDTOS;
    }

    @Mappings({
            @Mapping(source = "avatar", target = "avatarDTO", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT),
            @Mapping(target = "userMeta", expression = "java(UserMetaMapper.getMetaMapper(user, this.userCommonService))")
    })
    abstract public UserProfileDTO toOwnDTO(final User user);

    abstract public UserDTO toOthersDTOsFromProjection(final UserProjection user);

    public List<UserDTO> toOthersDTOsFromProjections(final List<UserProjection> users) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (UserProjection user : users) {
            userDTOS.add(this.toOthersDTOsFromProjection(user));
        }
        return userDTOS;
    }

    abstract public User fromUserRequest(final UserRequest userRequest);

    abstract public BioUserDTO toBioUserDTO(final User user);
}
