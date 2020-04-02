package com.questnr.services.user;

import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.UserRepository;
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

    public Page<PostAction> getUserFeed(Pageable pageable) {
        User user = userCommonService.getUser();
//        Page<Long> postActionIdList = postActionRepository.findByFollowingToUserActorAndJoinedWithCommunity(user, pageable);
//        List<PostAction> postActionList = new ArrayList<>();
//        for(Long postActionId: postActionIdList.getContent()){
//            postActionList.add(postActionRepository.findByPostActionId(postActionId));
//        }
//        return new PageImpl<>(postActionList, pageable, postActionIdList.getTotalElements());
        return postActionRepository.findByFollowingToUserActorAndJoinedWithCommunity(user, pageable);
    }
}
