package com.questnr.services.user;

import com.questnr.common.enums.PostActionType;
import com.questnr.common.enums.PostType;
import com.questnr.model.dto.PostBaseDTO;
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

import java.util.ArrayList;
import java.util.List;

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

    public Page<PostBaseDTO> getUserFeed(Pageable pageable) {
        User user = userCommonService.getUser();
        List<Object[]> postActionList = postActionRepository.getUserFeed(user.getUserId(), pageable.getPageSize() * pageable.getPageNumber(), pageable.getPageSize());

        List<PostBaseDTO> postActionFeedDTOList = new ArrayList<>();
        for (Object[] object : postActionList) {
            User userWhoShared;
            PostActionType postActionType;
            PostAction postAction = postActionRepository.findByPostActionId(Long.parseLong(object[0].toString()));
            if (Integer.parseInt(object[1].toString()) == 1) {
                userWhoShared = userCommonService.getUser(Long.parseLong(object[2].toString()));
                postActionType = PostActionType.shared;
            } else {
                userWhoShared = null;
                postActionType = PostActionType.normal;
            }
            if (postAction.getPostType() == PostType.simple) {
                postActionFeedDTOList.add(postActionMapper.toPostActionFeedDTO(
                        postAction,
                        postActionType,
                        userWhoShared
                ));
            } else {
                postActionFeedDTOList.add(postActionMapper.toPostPollQuestionFeedDTO(
                        postAction,
                        postActionType,
                        userWhoShared
                ));
            }
        }
        return new PageImpl<>(postActionFeedDTOList, pageable, postActionFeedDTOList.size());
    }
}
