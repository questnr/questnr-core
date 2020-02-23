package com.questnr.services;

import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.responses.PostActionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class TrendingPostService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CommonUserService commonUserService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostActionRepository postActionRepository;

    public List<PostActionResponse> getTrendingPosts(Pageable pageable) {
        Long userId = commonUserService.getUserId();
//        PostActionSpecificationBuilder builder = new PostActionSpecificationBuilder();
//        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "like"));
//        return postActionRepository.findAll(builder.build(new LinkedList<Filter>()), PostActionProjection.class, pageable);
        List<Object[]> objects = postActionRepository.findAllByTrendingPost(pageable);
        List<PostActionResponse> postActionResponseSet = new LinkedList<>();
        for (Object[] object : objects) {
            PostActionResponse postActionResponse = new PostActionResponse(postActionRepository.findByPostActionId(Long.parseLong(object[0].toString())));
            postActionResponse.setTotalLikes(Integer.parseInt(object[1].toString()));
            postActionResponse.setTotalPostVisits(Integer.parseInt(object[2].toString()));
            postActionResponse.setTotalComments(Integer.parseInt(object[3].toString()));
            postActionResponseSet.add(postActionResponse);
        }
        return postActionResponseSet;
    }
}
