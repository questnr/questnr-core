package com.questnr.services;

import com.questnr.model.entities.Community;
import com.questnr.model.repositories.CommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserHomeService {

    @Autowired
    CommunityRepository communityRepository;

    public List<Community> getTrendingCommunityList(Pageable pageable) {
        return new ArrayList<>();
    }
}
