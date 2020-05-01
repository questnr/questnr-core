package com.questnr.model.mapper;

import com.questnr.model.dto.LikeActionDTO;
import com.questnr.model.entities.LikeAction;
import com.questnr.model.entities.User;
import com.questnr.model.entities.UserFollower;
import com.questnr.model.repositories.UserFollowerRepository;
import com.questnr.services.user.UserCommonService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LikeActionDefaultMapper {

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    LikeActionMapper likeActionMapper;

    @Autowired
    UserFollowerRepository userFollowerRepository;

    LikeActionDefaultMapper() {
        likeActionMapper = Mappers.getMapper(LikeActionMapper.class);
    }

    List<LikeActionDTO> toDefaultLikeActionDTOList(Set<LikeAction> likeActionSet) {
        try {
            User user = userCommonService.getUser();
            List<LikeAction> likeActionList = new ArrayList<>(likeActionSet);
            List<UserFollower> userFollowerList = userFollowerRepository.findAllByFollowingUser(user);
            List<User> userLikedList = likeActionList.stream().map(LikeAction::getUserActor).collect(Collectors.toList());
            List<User> userToShowList = userFollowerList.stream().map(UserFollower::getUser)
                    .collect(Collectors.toList()).stream()
                    .filter(userLikedList::contains).collect(Collectors.toList());
            List<LikeAction> likeToShowList = likeActionList.stream().filter(likeAction ->
                    userToShowList.contains(likeAction.getUserActor())
            ).collect(Collectors.toList());
            return likeActionMapper.toDTOs(likeToShowList.subList(0, likeToShowList.size() > 2 ? 2 : likeToShowList.size()));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
