package com.questnr.model.mapper;

import com.questnr.model.dto.UserSecondaryDetailsDTO;
import com.questnr.model.entities.UserSecondaryDetails;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public abstract class UserSecondaryDetailsMapper {

    abstract public UserSecondaryDetailsDTO toDTO(final UserSecondaryDetails userSecondaryDetails);
}
