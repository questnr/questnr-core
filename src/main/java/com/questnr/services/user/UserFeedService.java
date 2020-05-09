package com.questnr.services.user;

import com.questnr.common.enums.PostActionType;
import com.questnr.model.dto.PostActionFeedDTO;
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

    public Page<PostActionFeedDTO> getUserFeed(Pageable pageable) {
        User user = userCommonService.getUser();
        List<Object[]> postActionList = postActionRepository.getUserFeed(user.getUserId(), pageable.getPageSize() * pageable.getPageNumber(), pageable.getPageSize());

        List<PostActionFeedDTO> postActionFeedDTOList = new ArrayList<>();
        for (Object[] object : postActionList) {
            if (Integer.parseInt(object[1].toString()) == 1) {
                postActionFeedDTOList.add(postActionMapper.toFeedDTO(
                        postActionRepository.findByPostActionId(Long.parseLong(object[0].toString())),
                        PostActionType.shared,
                        userCommonService.getUser(Long.parseLong(object[2].toString()))
                ));
            } else {
                postActionFeedDTOList.add(postActionMapper.toFeedDTO(
                        postActionRepository.findByPostActionId(Long.parseLong(object[0].toString())),
                        PostActionType.normal,
                        null
                ));
            }
        }

        return new PageImpl<>(postActionFeedDTOList, pageable, postActionFeedDTOList.size());

    }
}
