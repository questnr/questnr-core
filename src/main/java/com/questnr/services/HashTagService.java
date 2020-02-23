package com.questnr.services;

import com.questnr.model.projections.HashTagProjection;
import com.questnr.model.repositories.HashTagRepository;
import com.questnr.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class HashTagService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository userRepository;

    @Autowired
    HashTagRepository hashTagRepository;

    public Set<HashTagProjection> searchHashTag(String hashTag){
        return hashTagRepository.findByHashTagValueContaining(hashTag);
    }
}
