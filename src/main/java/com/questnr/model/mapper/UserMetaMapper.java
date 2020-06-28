package com.questnr.model.mapper;

import com.questnr.common.enums.RelationShipType;
import com.questnr.model.dto.user.UserMetaDTO;
import com.questnr.model.entities.User;
import com.questnr.model.entities.UserFollower;
import com.questnr.services.user.UserCommonService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMetaMapper {

    public static UserMetaDTO getMetaMapper(final User user, final UserCommonService userCommonService) {
        try {
            Long userId = userCommonService.getUserId();
            UserMetaDTO userMetaDTO = new UserMetaDTO();
            if (user.getUserId().equals(userId)) {
                userMetaDTO.setRelationShipType(RelationShipType.owned);
            } else {
                List<UserFollower> userFollowerList = user.getThisBeingFollowedUserSet().stream().filter(userFollower ->
                        userFollower.getFollowingUser().getUserId().equals(userId)
                ).collect(Collectors.toList());
                if (userFollowerList.size() == 1) {
                    userMetaDTO.setRelationShipType(RelationShipType.followed);
                }
            }
            return userMetaDTO;
        } catch (Exception e) {
            return new UserMetaDTO();
        }
    }
}
