package com.questnr.services.user;

import com.questnr.model.dto.CommunityDTO;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityInvitedUser;
import com.questnr.model.entities.CommunityUser;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.services.CustomPageService;
import com.questnr.services.community.CommunityCommonService;
import org.mapstruct.factory.Mappers;
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
public class UserJoinService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CommunityCommonService communityCommonService;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomPageService<Community> customPageService;

    public Page<Community> getCommunityJoinedList(Pageable pageable) {
        List<CommunityUser> communityUserList = new ArrayList<>(userCommonService.getUser().getCommunityJoinedList());

        Comparator<CommunityUser> communityUserComparator = Comparator.comparing(CommunityUser::getCreatedAt);
        communityUserList.sort(communityUserComparator.reversed());

        List<Community> communities = communityUserList.stream().map(CommunityUser::getCommunity).collect(Collectors.toList());

        return customPageService.customPage(communities, pageable);
    }

    public Page<Community> getCommunityInvitationList(Pageable pageable) {
        List<CommunityInvitedUser> communityInvitedUserList = new ArrayList<>(userCommonService.getUser().getCommunityInvitedUsers());

        Comparator<CommunityInvitedUser> communityInvitedUserComparator = Comparator.comparing(CommunityInvitedUser::getCreatedAt);
        communityInvitedUserList.sort(communityInvitedUserComparator.reversed());

        List<Community> communities = communityInvitedUserList.stream().map(CommunityInvitedUser::getCommunity).collect(Collectors.toList());

        return customPageService.customPage(communities, pageable);
    }
}
