package com.questnr.services;

import com.questnr.model.dto.TrendingPostDTO;
import com.questnr.model.mapper.TrendingPostMapper;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.services.user.UserCommonService;
import org.mapstruct.factory.Mappers;
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
    UserCommonService userCommonService;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    final TrendingPostMapper trendingPostMapper;

    @Autowired
    UserRepository userRepository;

    TrendingPostService(){
        trendingPostMapper = Mappers.getMapper(TrendingPostMapper.class);
    }

    public List<TrendingPostDTO> getTrendingPosts(Pageable pageable) {
//        PostActionSpecificationBuilder builder = new PostActionSpecificationBuilder();
//        PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "like"));
//        return postActionRepository.findAll(builder.build(new LinkedList<Filter>()), PostActionProjection.class, pageable);
        List<Object[]> objects = postActionRepository.findAllByTrendingPost(pageable);
        List<TrendingPostDTO> trendingPostDTOList = new LinkedList<>();
        for (Object[] object : objects) {
            TrendingPostDTO trendingPostDTO = trendingPostMapper.toDTO(postActionRepository.findByPostActionId(Long.parseLong(object[0].toString())));
            trendingPostDTO.setTotalLikes(Integer.parseInt(object[1].toString()));
            trendingPostDTO.setTotalPostVisits(Integer.parseInt(object[2].toString()));
            trendingPostDTO.setTotalComments(Integer.parseInt(object[3].toString()));
            trendingPostDTOList.add(trendingPostDTO);
        }
        return trendingPostDTOList;
    }
}
