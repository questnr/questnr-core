package com.questnr.controllers.community;

import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.dto.PostActionForCommunityDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.model.dto.PostActionRequestDTO;
import com.questnr.services.community.CommunityPostActionService;
import com.questnr.services.PostActionService;
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
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class CommunityPostActionController {

    @Autowired
    PostActionService postActionService;

    @Autowired
    CommunityPostActionService communityPostActionService;

    @Autowired
    final PostActionMapper postActionMapper;

    CommunityPostActionController() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    // Basic post operations for users.
    @RequestMapping(value = "community/{communityId}/posts", method = RequestMethod.GET)
    Page<PostActionForCommunityDTO> getAllPostsByCommunityId(@PathVariable long communityId, Pageable pageable) {
        Page<PostAction> page = communityPostActionService.getAllPostActionsByCommunityId(communityId, pageable);
        return new PageImpl<PostActionForCommunityDTO>(postActionMapper.toPostActionForCommunityDTOs(page.getContent()), pageable, page.getTotalElements());
    }

    @RequestMapping(value = "community/{communityId}/posts", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    PostActionDTO createPost(@PathVariable long communityId, @Valid PostActionRequestDTO postActionRequestDTO, @RequestParam(value = "file") List<MultipartFile> files) {
        return postActionMapper.toDTO(communityPostActionService.creatPostAction(postActionMapper.fromPostActionRequestDTO(postActionRequestDTO), files, communityId));
    }

    @RequestMapping(value = "community/{communityId}/posts/{postId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    void updatePost(@PathVariable long communityId, @PathVariable Long postId, @Valid @RequestBody PostActionRequestDTO postActionRequest) {
        communityPostActionService.updatePostAction(communityId, postId,  postActionMapper.fromPostActionRequestDTO(postActionRequest));
    }

    @RequestMapping(value = "community/{communityId}/posts/{postId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void deletePost(@PathVariable long communityId, @PathVariable Long postId) {
        communityPostActionService.deletePostAction(communityId, postId);
    }
}
