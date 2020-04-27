package com.questnr.model.mapper;

import com.questnr.common.enums.RelationShipType;
import com.questnr.model.dto.CommunityMetaDTO;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityUser;
import com.questnr.services.user.UserCommonService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommunityMetaMapper {

    public static CommunityMetaDTO getMetaMapper(final Community community, final UserCommonService userCommonService) {
        try {
            Long userId = userCommonService.getUserId();
            CommunityMetaDTO communityMetaDTO = new CommunityMetaDTO();
            if (community.getOwnerUser().getUserId().equals(userCommonService.getUserId())) {
                communityMetaDTO.setRelationShipType(RelationShipType.owned);
            } else {
                List<CommunityUser> communityUserList = community.getUsers().stream().filter(communityUser ->
                        communityUser.getUser().getUserId().equals(userId)
                ).collect(Collectors.toList());
                if (communityUserList.size() == 1) {
                    communityMetaDTO.setRelationShipType(RelationShipType.followed);
                }
            }
            return communityMetaDTO;
        } catch (Exception e) {
            return new CommunityMetaDTO();
        }
    }
}
