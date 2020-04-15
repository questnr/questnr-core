package com.questnr.services;

import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostActionTrendLinearData;
import com.questnr.model.repositories.PostActionTrendLinearDataRepository;
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
import java.util.stream.Collectors;

@Service
public class TrendingPostService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    PostActionTrendLinearDataRepository postActionTrendLinearDataRepository;

    public Page<PostAction> getTrendingPostList(Pageable pageable) {
        Page<PostActionTrendLinearData> postActionTrendLinearDataPage = postActionTrendLinearDataRepository.findAll(pageable);
        // List sorted with descending order of regression slope
        Comparator<PostActionTrendLinearData> postActionTrendLinearDataComparator
                = Comparator.comparing(PostActionTrendLinearData::getSlop);

        List<PostActionTrendLinearData> postActionTrendLinearDataList = new ArrayList<>(postActionTrendLinearDataPage.getContent());
        postActionTrendLinearDataList.sort(postActionTrendLinearDataComparator.reversed());

        return new PageImpl<>(postActionTrendLinearDataList.stream().map(PostActionTrendLinearData::getPostAction).collect(Collectors.toList()), pageable, postActionTrendLinearDataPage.getTotalElements());
    }
}
