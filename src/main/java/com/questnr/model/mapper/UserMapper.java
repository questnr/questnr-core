package com.questnr.model.mapper;

import com.questnr.model.dto.UserDTO;
import com.questnr.model.entities.User;
import com.questnr.model.projections.UserProjection;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {

    UserDTO toOthersDTO(final User user);

    List<UserDTO> toOthersDTOs(final List<User> users);

    List<UserDTO> toOthersDTOsFromProjections(final List<UserProjection> users);
}
