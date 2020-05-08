package com.questnr.controllers.community;

import com.questnr.access.CommunityPostActionAccessService;
import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.dto.PostActionForCommunityDTO;
import com.questnr.model.dto.PostActionRequestDTO;
import com.questnr.model.dto.PostActionUpdateRequestDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.services.CommonService;
import com.questnr.services.PostActionService;
import com.questnr.services.community.CommunityPostActionService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/user")
public class CommunityPostActionController {

    @Autowired
    CommunityPostActionService communityPostActionService;

    @Autowired
    CommunityPostActionAccessService communityPostActionAccessService;

    @Autowired
    final PostActionMapper postActionMapper;

    @Autowired
    PostActionService postActionService;

    @Autowired
    CommonService commonService;

    CommunityPostActionController() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    // Basic post operations for users.
    @RequestMapping(value = "/community/{communityId}/posts", method = RequestMethod.GET)
    Page<PostActionForCommunityDTO> getAllPostsByCommunityId(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size, @PathVariable long communityId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PostAction> postActionPage = communityPostActionService.getAllPostActionsByCommunityId(communityId, pageable);
        List<PostActionForCommunityDTO> postActionForCommunityDTOS = postActionPage.getContent().stream().map(postActionMapper::toPostActionForCommunityDTO).collect(Collectors.toList());
        return new PageImpl<>(postActionForCommunityDTOS, pageable, postActionPage.getTotalElements());
    }

    @RequestMapping(value = "/community/{communityId}/posts", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    PostActionDTO createPost(@PathVariable long communityId, PostActionRequestDTO postActionRequestDTO) {
        /*
         * Community Post Security Checking
         * */
        if (communityPostActionAccessService.hasAccessToPostCreation(communityId)) {
            if (postActionRequestDTO.getFiles() != null && postActionRequestDTO.getFiles().size() > 0) {
                return communityPostActionService.creatPostAction(postActionMapper.fromPostActionRequestDTO(postActionRequestDTO), postActionRequestDTO.getFiles(), communityId);
            } else {
                return communityPostActionService.creatPostAction(postActionMapper.fromPostActionRequestDTO(postActionRequestDTO), communityId);
            }
        }
        throw new AccessException();
    }

    @RequestMapping(value = "/community/{communityId}/posts/{postId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    void updatePost(@PathVariable long communityId, @PathVariable Long postId, @Valid @RequestBody PostActionUpdateRequestDTO postActionRequest) {
        /*
         * Community Post Security Checking
         * */
        PostAction postAction = communityPostActionAccessService.hasAccessToPostModification(communityId, postId);
        postActionService.updatePostAction(postAction, postActionRequest);
    }

    @RequestMapping(value = "/community/{communityId}/posts/{postId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void deletePost(@PathVariable long communityId, @PathVariable Long postId) {
        /*
         * Community Post Security Checking
         * */
        PostAction postAction = communityPostActionAccessService.hasAccessToPostDeletion(communityId, postId);
        postActionService.deletePostAction(postAction);
    }

}
