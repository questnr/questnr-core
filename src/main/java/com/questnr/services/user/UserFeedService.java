package com.questnr.services.user;

import com.questnr.access.PostActionAccessService;
import com.questnr.exceptions.InvalidRequestException;
import com.questnr.model.dto.post.PostBaseDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.services.PostActionService;
import com.questnr.services.PostActionUserViewableService;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    PostActionUserViewableService postActionUserViewableService;

    @Autowired
    PostActionAccessService postActionAccessService;

    @Autowired
    PostActionService postActionService;

    UserFeedService() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    public Page<PostBaseDTO> getUserFeed(Pageable pageable) {
        User user = userCommonService.getUser();
        List<Object[]> postActionList = postActionRepository.getUserFeed(user.getUserId(), pageable.getPageSize() * pageable.getPageNumber(), pageable.getPageSize());
        return this.postActionUserViewableService.getUserViewablePosts(postActionList, pageable);
    }

    public Page<PostBaseDTO> getUserFeedUsingId(String posts,
                                                Long lastPostId,
                                                Pageable pageable) {
        User user = userCommonService.getUser();
        if (lastPostId != null) {
            PostAction postAction = postActionService.getPostActionById(lastPostId);
            if (postAction != null) {
                List<Object[]> postActionList = postActionRepository.getUserFeedByLastPost(
                        user.getUserId(),
                        postAction.getCreatedAt());
                List<String> postIdList = postActionList.stream().map(objects ->
                        objects[0].toString()
                ).filter(postId ->
                        !postAction.getPostActionId().equals(Long.parseLong(postId))
                ).collect(Collectors.toList());
                List<String> slicedPostIdList = postIdList.subList(0, Math.min(4, postIdList.size()));
                return this.postActionUserViewableService.getPostBaseDTOPageFromPostId(slicedPostIdList,
                        pageable, postIdList.size());
            }
            throw new InvalidRequestException();
        } else {
            List<String> postIdStringList = Arrays.asList(posts.split(","));
            return this.postActionUserViewableService.getPostBaseDTOPageFromPostId(postIdStringList, pageable);
        }
    }
}