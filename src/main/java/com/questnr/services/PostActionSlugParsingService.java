package com.questnr.services;

import com.questnr.common.enums.PostType;
import com.questnr.common.message.helper.messages.PostActionMessages;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.dto.post.PostBaseDTO;
import com.questnr.model.dto.post.normal.PostNotAccessibleDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.model.mapper.PostActionMapper;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostActionSlugParsingService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    PostActionMapper postActionMapper;

    @Autowired
    CommunityMapper communityMapper;

    @Autowired
            PostActionService postActionService;

    @Autowired
    PostActionMetaService postActionMetaService;

    PostActionSlugParsingService() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
        communityMapper = Mappers.getMapper(CommunityMapper.class);
    }

    public PostBaseDTO getPostBase(String postSlug){
        PostAction postAction = postActionService.getPostActionFromSlug(postSlug);
        if (postAction.getPostType() == PostType.simple) {
            return postActionMetaService.setPostActionMetaInformation(postActionMapper.toSinglePostPublicDTO(postAction));
        } else if (postAction.getPostType() == PostType.question) {
            return postActionMetaService.setPostActionMetaInformation(postActionMapper.toPollQuestionPublicDTO(postAction));
        }
        throw new ResourceNotFoundException(PostActionMessages.PA100);
    }

    public PostNotAccessibleDTO postNotAccessible(String postSlug){
        PostAction postAction = postActionService.getPostActionFromSlug(postSlug);
        if (postAction.getPostType() == PostType.simple) {
            PostNotAccessibleDTO postBaseDTO = new PostNotAccessibleDTO();
            postBaseDTO.setCommunityDTO(communityMapper.toDTO(postAction.getCommunity()));
            return postBaseDTO;
        } else if (postAction.getPostType() == PostType.question) {
            PostNotAccessibleDTO postBaseDTO = new PostNotAccessibleDTO();
            postBaseDTO.setCommunityDTO(communityMapper.toDTO(postAction.getCommunity()));
            return postBaseDTO;
        }
        throw new ResourceNotFoundException(PostActionMessages.PA100);
    }
}
