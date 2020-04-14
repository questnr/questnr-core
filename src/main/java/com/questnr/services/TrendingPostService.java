package com.questnr.services;

import com.questnr.common.StartingEndingDate;
import com.questnr.model.dto.TrendingPostDTO;
import com.questnr.model.mapper.TrendingPostMapper;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.services.user.UserCommonService;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
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

    TrendingPostService() {
        trendingPostMapper = Mappers.getMapper(TrendingPostMapper.class);
    }

    private List<TrendingPostDTO> calculateTrend(List<TrendingPostDTO> trendingPostDTOList) {
        return trendingPostDTOList;
    }

    private Page<TrendingPostDTO> getTrendingPostData(Date startingDate, Date endingDate, Pageable pageable) {
        Page<Object[]> page = postActionRepository.findAllByTrendingPost(startingDate, endingDate, pageable);
        List<TrendingPostDTO> trendingPostDTOList = new LinkedList<>();
        List<Object[]> trendingData = page.getContent();
        for (Object[] object : trendingData) {
            TrendingPostDTO trendingPostDTO = trendingPostMapper.toDTO(postActionRepository.findByPostActionId(Long.parseLong(object[0].toString())));
            trendingPostDTO.setTotalTrendingLikes(Integer.parseInt(object[1].toString()));
            trendingPostDTO.setTotalTrendingComments(Integer.parseInt(object[2].toString()));
            trendingPostDTO.setTotalTrendingPostVisits(Integer.parseInt(object[3].toString()));
            trendingPostDTOList.add(trendingPostDTO);
        }
        return new PageImpl<>(this.calculateTrend(trendingPostDTOList), pageable, page.getTotalElements());
    }

    public Page<TrendingPostDTO> getTrendingPostsOfTheWeek(Pageable pageable) {
//        Date startingDate = dateFormat.parse("2020-03-30");

        StartingEndingDate startingEndingDate = new StartingEndingDate();
        startingEndingDate.setDaysBefore(7);
        startingEndingDate.build();

        return this.getTrendingPostData(startingEndingDate.getStartingDate(), startingEndingDate.getEndingDate(), pageable);
    }
}
