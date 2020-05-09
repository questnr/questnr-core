package com.questnr.services;

import com.questnr.common.UserRankDependents;
import com.questnr.model.dto.HashTagWithRankDTO;
import com.questnr.model.dto.UserWithRankDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.HashTagWithRankMapper;
import com.questnr.model.mapper.UserWithRankMapper;
import com.questnr.model.repositories.HashTagRepository;
import com.questnr.model.repositories.UserRepository;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LandingPageService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserWithRankMapper userWithRankMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HashTagWithRankMapper hashTagWithRankMapper;

    @Autowired
    HashTagRepository hashTagRepository;

    LandingPageService() {
        userWithRankMapper = Mappers.getMapper(UserWithRankMapper.class);
        hashTagWithRankMapper = Mappers.getMapper(HashTagWithRankMapper.class);
    }

    UserWithRankDTO calculateUserRank(UserWithRankDTO userWithRankDTO, User user) {
        Set<PostAction> postActionList = user.getPostActionSet();
        int totalPosts = postActionList.size();
        int totalLikes = 0;
        int totalComments = 0;
        int totalPostVisits = 0;
        for (PostAction postAction : postActionList) {
            totalLikes += postAction.getLikeActionSet().size();
            totalComments += postAction.getCommentActionSet().size();
            totalPostVisits += postAction.getPostVisitSet().size();
        }
        Double userRank = userWithRankDTO.getTotalFollowers() * UserRankDependents.USER_FOLLOWERS +
                totalPosts * UserRankDependents.POST_ACTION +
                totalLikes * UserRankDependents.LIKE_ACTION +
                totalComments * UserRankDependents.COMMENT_ACTION +
                totalPostVisits * UserRankDependents.POST_VISIT;

        userWithRankDTO.setTotalPosts(totalPosts);
        userWithRankDTO.setTotalLikes(totalLikes);
        userWithRankDTO.setTotalComments(totalComments);
        userWithRankDTO.setTotalPostVisits(totalPostVisits);
        userWithRankDTO.setUserRank(userRank.longValue());
        return userWithRankDTO;
    }

//    public Page<UserWithRankDTO> getUsersWithHighestRank(Pageable pageable) {
//        Page<Object[]> objects = userRepository.findAllByUserFollowerContaining(pageable);
//        List<UserWithRankDTO> userWithRankDTOS = new ArrayList<>();
//        for (Object[] object : objects.getContent()) {
//            User user = userRepository.findByUserId(Long.parseLong(object[0].toString()));
//            UserWithRankDTO userWithRankDTO = userWithRankMapper.toUserWithRankDTO(user);
//            userWithRankDTO.setTotalFollowers(Integer.parseInt(object[1].toString()));
//            userWithRankDTOS.add(this.calculateUserRank(userWithRankDTO, user));
//        }
//        // List sorted with descending order of user rank
//        Comparator<UserWithRankDTO> userRankComparator
//                = Comparator.comparing(UserWithRankDTO::getUserRank);
//        userWithRankDTOS.sort(userRankComparator.reversed());
//        return new PageImpl<>(userWithRankDTOS, pageable, objects.getTotalElements());
//    }

    public Page<HashTagWithRankDTO> getHashTagsWithHighestRank(Pageable pageable) {
        Page<Object[]> objects = hashTagRepository.findAllByHashTagForRank(pageable);
        List<HashTagWithRankDTO> hashTagWithRankDTOList = new ArrayList<>();
        for(Object[] object: objects.getContent()) {
            HashTagWithRankDTO hashTagWithRankDTO = new HashTagWithRankDTO();
            hashTagWithRankDTO.setHashTagValue(object[0].toString());
            hashTagWithRankDTO.setHashTagRank(Long.parseLong(object[1].toString()));
            hashTagWithRankDTOList.add(hashTagWithRankDTO);
        }
        return new PageImpl<>(hashTagWithRankDTOList, pageable, objects.getTotalElements());
    }
}
