package com.questnr.services;

import com.questnr.model.dto.HashTagWithRankDTO;
import com.questnr.model.dto.UserWithRankDTO;
import com.questnr.model.entities.User;
import com.questnr.model.entities.UserTrendLinearData;
import com.questnr.model.mapper.HashTagWithRankMapper;
import com.questnr.model.mapper.UserWithRankMapper;
import com.questnr.model.repositories.*;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    UserFollowerRepository userFollowerRepository;

    @Autowired
    UserTrendLinearDataRepository userTrendLinearDataRepository;

    LandingPageService() {
        userWithRankMapper = Mappers.getMapper(UserWithRankMapper.class);
        hashTagWithRankMapper = Mappers.getMapper(HashTagWithRankMapper.class);
    }

//    UserWithRankDTO calculateUserRank(UserWithRankDTO userWithRankDTO, User user) {
//        Set<PostAction> postActionList = user.getPostActionSet();
//        int totalPosts = postActionList.size();
//        int totalLikes = 0;
//        int totalComments = 0;
//        int totalPostVisits = 0;
//        for (PostAction postAction : postActionList) {
//            totalLikes += postAction.getLikeActionSet().size();
//            totalComments += postAction.getCommentActionSet().size();
//            totalPostVisits += postAction.getPostVisitSet().size();
//        }
//        Double userRank = userWithRankDTO.getTotalFollowers() * UserRankDependents.USER_FOLLOWERS +
//                totalPosts * UserRankDependents.POST_ACTION +
//                totalLikes * UserRankDependents.LIKE_ACTION +
//                totalComments * UserRankDependents.COMMENT_ACTION +
//                totalPostVisits * UserRankDependents.POST_VISIT;
//
//        userWithRankDTO.setTotalPosts(totalPosts);
//        userWithRankDTO.setTotalLikes(totalLikes);
//        userWithRankDTO.setTotalComments(totalComments);
//        userWithRankDTO.setTotalPostVisits(totalPostVisits);
//        userWithRankDTO.setUserRank(userRank.longValue());
//        return userWithRankDTO;
//    }

    public Page<UserWithRankDTO> getUsersWithHighestRank(Pageable pageable) {
        Page<UserTrendLinearData> userTrendLinearDataPage = userTrendLinearDataRepository.findAll(pageable);
        List<UserWithRankDTO> userWithRankDTOS = new ArrayList<>();
        for (UserTrendLinearData userTrendLinearData : userTrendLinearDataPage.getContent()) {
            User user = userTrendLinearData.getUser();
            UserWithRankDTO userWithRankDTO = userWithRankMapper.toUserWithRankDTO(user);
            userWithRankDTO.setTotalPosts(postActionRepository.countByUserActor(user));
            userWithRankDTO.setTotalFollowers(userFollowerRepository.countByUser(user));
            userWithRankDTO.setUserRank(userTrendLinearData.getPoints());
            userWithRankDTOS.add(userWithRankDTO);
        }
        // List sorted with descending order of user rank
        Comparator<UserWithRankDTO> userRankComparator
                = Comparator.comparing(UserWithRankDTO::getUserRank);
        userWithRankDTOS.sort(userRankComparator.reversed());
        return new PageImpl<>(userWithRankDTOS, pageable, userTrendLinearDataPage.getTotalElements());
    }

    public Page<HashTagWithRankDTO> getHashTagsWithHighestRank(Pageable pageable) {
        Page<Object[]> objects = hashTagRepository.findAllByHashTagForRank(pageable);
        List<HashTagWithRankDTO> hashTagWithRankDTOList = new ArrayList<>();
        for (Object[] object : objects.getContent()) {
            HashTagWithRankDTO hashTagWithRankDTO = new HashTagWithRankDTO();
            hashTagWithRankDTO.setHashTagValue(object[0].toString());
            hashTagWithRankDTO.setHashTagRank(Long.parseLong(object[1].toString()));
            hashTagWithRankDTOList.add(hashTagWithRankDTO);
        }
        return new PageImpl<>(hashTagWithRankDTOList, pageable, objects.getTotalElements());
    }
}
