package com.questnr.model.mapper;

import com.questnr.model.dto.UserNotificationSettingsDTO;
import com.questnr.model.entities.UserNotificationSettings;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserNotificationSettingsMapper {
    UserNotificationSettingsDTO toDTO(UserNotificationSettings userNotificationSettings);
}
