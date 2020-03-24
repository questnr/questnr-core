package com.questnr.controllers.community;

import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.dto.PostActionForCommunityDTO;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.PostAction;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.model.dto.PostActionRequestDTO;
import com.questnr.services.community.CommunityPostActionService;
import com.questnr.services.PostActionService;
import com.questnr.services.community.CommunitySecurityService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1")
public class CommunityPostActionController {

    @Autowired
    CommunityPostActionService communityPostActionService;

    @Autowired
    CommunitySecurityService communitySecurityService;

    @Autowired
    final PostActionMapper postActionMapper;

    @Autowired
    PostActionService postActionService;

    CommunityPostActionController() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    // Basic post operations for users.
    @RequestMapping(value = "community/{communityId}/posts", method = RequestMethod.GET)
    Page<PostActionForCommunityDTO> getAllPostsByCommunityId(@PathVariable long communityId, Pageable pageable) {
        Page<PostAction> page = communityPostActionService.getAllPostActionsByCommunityId(communityId, pageable);
        List<PostActionForCommunityDTO> postActionForCommunityDTOS = page.getContent().stream().map(postAction ->
                postActionMapper.toPostActionForCommunityDTO(postAction)
        ).collect(Collectors.toList());
        return new PageImpl<PostActionForCommunityDTO>(postActionForCommunityDTOS, pageable, page.getTotalElements());
    }

    @RequestMapping(value = "community/{communityId}/posts", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    PostActionDTO createPost(@PathVariable long communityId, @Valid PostActionRequestDTO postActionRequestDTO, @RequestParam(value = "file") List<MultipartFile> files) {
        if (communitySecurityService.hasAccessToCommunity(communityId))
            return postActionMapper.toDTO(communityPostActionService.creatPostAction(postActionMapper.fromPostActionRequestDTO(postActionRequestDTO), files, communityId));
        return null;
    }

    @RequestMapping(value = "community/{communityId}/posts/{postId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    void updatePost(@PathVariable long communityId, @PathVariable Long postId, @Valid @RequestBody PostActionRequestDTO postActionRequest) {
        if (communitySecurityService.hasAccessToCommunity(communityId))
            communityPostActionService.updatePostAction(communityId, postId, postActionMapper.fromPostActionRequestDTO(postActionRequest));
    }

    @RequestMapping(value = "community/{communityId}/posts/{postId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void deletePost(@PathVariable long communityId, @PathVariable Long postId) {
        if (communitySecurityService.hasAccessToCommunity(communityId))
            communityPostActionService.deletePostAction(communityId, postId);
    }
}
