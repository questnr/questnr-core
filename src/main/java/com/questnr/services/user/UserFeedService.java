package com.questnr.services.user;

import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.UserRepository;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserFeedService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostActionMapper postActionMapper;

    UserFeedService() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    public Page<PostActionDTO> getUserFeed(Pageable pageable) {
        User user = userCommonService.getUser();
        Page<PostAction> postActionPage = postActionRepository.findByFollowingToUserActorAndJoinedWithCommunity(user, pageable);
        return new PageImpl<>(postActionMapper.toDTOs(postActionPage.getContent(), userCommonService.getUser()), pageable, postActionPage.getTotalElements());

    }
}
