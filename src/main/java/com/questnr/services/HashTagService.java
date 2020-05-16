package com.questnr.services;

import com.questnr.model.dto.PostActionPublicDTO;
import com.questnr.model.entities.HashTag;
import com.questnr.model.entities.HashTagTrendLinearData;
import com.questnr.model.entities.PostAction;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.model.projections.HashTagProjection;
import com.questnr.model.repositories.HashTagRepository;
import com.questnr.model.repositories.HashTagTrendLinearDataRepository;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.UserRepository;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HashTagService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository userRepository;

    @Autowired
    HashTagRepository hashTagRepository;

    @Autowired
    HashTagTrendLinearDataRepository hashTagTrendLinearDataRepository;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    PostActionMapper postActionMapper;

    HashTagService() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    public Set<HashTagProjection> searchHashTag(String hashTag) {
        return hashTagRepository.findByHashTagValueContaining(hashTag.toLowerCase());
    }

    public Page<HashTag> getTrendingHashTagList(Pageable pageable) {
        Page<HashTagTrendLinearData> hashTagTrendLinearDataPage = hashTagTrendLinearDataRepository.findAll(pageable);

        return new PageImpl<>(hashTagTrendLinearDataPage.getContent().stream().map(HashTagTrendLinearData::getHashTag).collect(Collectors.toList()), pageable, hashTagTrendLinearDataPage.getTotalElements());
    }

    public Page<PostActionPublicDTO> getPostActionListUsingHashTag(String hashTagValue, Pageable pageable) {

        Page<PostAction> postActionPage = postActionRepository.findByHashTags(hashTagRepository.findByHashTagValue(hashTagValue), pageable);
        return new PageImpl<>(postActionMapper.toPublicDTOs(postActionPage.getContent()), pageable, postActionPage.getTotalElements());
    }
}
