package com.questnr.services;

import com.questnr.common.CommunitySuggestionData;
import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.entities.*;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.model.repositories.CommunityTrendLinearDataRepository;
import com.questnr.model.repositories.HashTagTrendLinearDataRepository;
import com.questnr.model.repositories.PostActionTrendLinearDataRepository;
import com.questnr.services.user.UserCommonService;
import info.debatty.java.stringsimilarity.Cosine;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserHomeService {

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    CommonService commonService;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    CommunityTrendLinearDataRepository communityTrendLinearDataRepository;

    @Autowired
    PostActionTrendLinearDataRepository postActionTrendLinearDataRepository;

    @Autowired
    HashTagTrendLinearDataRepository hashTagTrendLinearDataRepository;

    private Cosine cosine;

    @Autowired
    PostActionMapper postActionMapper;

    UserHomeService() {
        cosine = new Cosine();
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    public Page<Community> getTrendingCommunityList(Pageable pageable) {
        Page<CommunityTrendLinearData> communityTrendLinearDataPage = communityTrendLinearDataRepository.findAll(pageable);
        // List sorted with descending order of regression slope
        Comparator<CommunityTrendLinearData> communityTrendLinearDataComparator
                = Comparator.comparing(CommunityTrendLinearData::getSlop);

        List<CommunityTrendLinearData> communityTrendLinearDataList = new ArrayList<>(communityTrendLinearDataPage.getContent());
        communityTrendLinearDataList.sort(communityTrendLinearDataComparator.reversed());

        return new PageImpl<>(communityTrendLinearDataList.stream().map(CommunityTrendLinearData::getCommunity).collect(Collectors.toList()), pageable, communityTrendLinearDataPage.getTotalElements());
    }

//    private List<Community> determineCommunitySuggestedList(List<CommunitySuggestionData> communitySuggestionDataList){
//
//
//
//
//        return new ArrayList<>();
//    }


    public Page<Community> getSuggestedCommunityList(Pageable pageable) {
        User user = userCommonService.getUser();

        // Joined communities by this user.
        List<Community> thisUserJoinedCommunityList = commonService.getCommunityList(user.getCommunityJoinedList());


        // Joined communities by the users which are being followed from this user.
        List<User> followedUsers = user.getThisFollowingUserSet().stream().map(UserFollower::getUser).collect(Collectors.toList());
        Set<Community> communityList = new HashSet<>();
        for (User followedUser : followedUsers) {
            communityList.addAll(commonService.getCommunityList(followedUser.getCommunityJoinedList()));
        }

        // Get Trending Communities
        List<Community> trendingCommunityList = this.getTrendingCommunityList(pageable).getContent();
        communityList.addAll(trendingCommunityList);

        // Remove community which are already joined by the user.
        List<Community> filteredCommunityList = communityList.stream().filter(community ->
                !thisUserJoinedCommunityList.contains(community)
        ).collect(Collectors.toList());

        List<CommunitySuggestionData> communitySuggestionDataList = new ArrayList<>();
        for (Community communitySuggested : filteredCommunityList) {
            for (Community joinedCommunity : thisUserJoinedCommunityList) {

                CommunitySuggestionData communitySuggestionData = new CommunitySuggestionData();
                communitySuggestionData.setCommunity(communitySuggested);
                communitySuggestionData.setSimilarity(cosine.similarity(joinedCommunity.getDescription(), communitySuggested.getDescription()));

                if (communitySuggestionDataList.contains(communitySuggestionData)) {
                    int indexOf = communitySuggestionDataList.indexOf(communitySuggestionData);
                    CommunitySuggestionData wasCommunitySuggestionData = communitySuggestionDataList.get(indexOf);
                    if (wasCommunitySuggestionData.getSimilarity() >= communitySuggestionData.getSimilarity()) {
                        continue;
                    } else {
                        communitySuggestionDataList.remove(wasCommunitySuggestionData);
                        communitySuggestionDataList.add(communitySuggestionData);
                    }
                } else {
                    communitySuggestionDataList.add(communitySuggestionData);
                }
            }
        }

        Comparator<CommunitySuggestionData> communitySuggestionDataComparator
                = Comparator.comparing(CommunitySuggestionData::getSimilarity);

        communitySuggestionDataList.sort(communitySuggestionDataComparator.reversed());

        List<Community> returnCommunityList = communitySuggestionDataList.stream()
                .map(CommunitySuggestionData::getCommunity)
                .collect(Collectors.toList());

        return new PageImpl<>(returnCommunityList.subList(
                0, returnCommunityList.size() > pageable.getPageSize() ?
                        pageable.getPageSize() :
                        returnCommunityList.size()
        ), pageable, returnCommunityList.size());
    }

    public Page<PostActionDTO> getTrendingPostList(Pageable pageable) {
        Page<PostActionTrendLinearData> postActionTrendLinearDataPage = postActionTrendLinearDataRepository.findAll(pageable);
        // List sorted with descending order of regression slope
        Comparator<PostActionTrendLinearData> postActionTrendLinearDataComparator
                = Comparator.comparing(PostActionTrendLinearData::getSlop);

        List<PostActionTrendLinearData> postActionTrendLinearDataList = new ArrayList<>(postActionTrendLinearDataPage.getContent());
        postActionTrendLinearDataList.sort(postActionTrendLinearDataComparator.reversed());

        return new PageImpl<>(postActionMapper.toDTOs(postActionTrendLinearDataList.stream().map(PostActionTrendLinearData::getPostAction).collect(Collectors.toList()), userCommonService.getUser()), pageable, postActionTrendLinearDataPage.getTotalElements());

    }
}
