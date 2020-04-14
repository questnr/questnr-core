package com.questnr.services;

import com.questnr.common.CommunityTrendLinearData;
import com.questnr.model.dto.UserWithRankDTO;
import com.questnr.model.entities.Community;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.model.repositories.CommunityTrendLinearDataRepository;
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
public class UserHomeService {

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    CommunityTrendLinearDataRepository communityTrendLinearDataRepository;

    public Page<Community> getTrendingCommunityList(Pageable pageable) {
        Page<CommunityTrendLinearData> communityTrendLinearDataPage = communityTrendLinearDataRepository.findAll(pageable);
        // List sorted with descending order of regression slope
        Comparator<CommunityTrendLinearData> communityTrendLinearDataComparator
                = Comparator.comparing(CommunityTrendLinearData::getSlop);

        List<CommunityTrendLinearData> communityTrendLinearDataList = new ArrayList<>(communityTrendLinearDataPage.getContent());
        communityTrendLinearDataList.sort(communityTrendLinearDataComparator.reversed());

        return new PageImpl<>(communityTrendLinearDataList.stream().map(CommunityTrendLinearData::getCommunity).collect(Collectors.toList()), pageable, communityTrendLinearDataPage.getTotalElements());
    }

    public Page<Community> getSuggestedCommunityList(Pageable pageable) {
        Page<CommunityTrendLinearData> communityTrendLinearDataPage = communityTrendLinearDataRepository.findAll(pageable);

        return new PageImpl<>(communityTrendLinearDataPage.getContent().stream().map(CommunityTrendLinearData::getCommunity).collect(Collectors.toList()), pageable, communityTrendLinearDataPage.getTotalElements());
    }
}
