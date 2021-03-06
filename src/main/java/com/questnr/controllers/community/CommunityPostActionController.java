package com.questnr.controllers.community;

import com.questnr.access.community.CommunityPostActionAccessService;
import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.post.PostBaseDTO;
import com.questnr.model.dto.post.normal.PostActionFeedDTO;
import com.questnr.model.dto.post.normal.PostActionRequestDTO;
import com.questnr.model.dto.post.normal.PostActionUpdateRequestDTO;
import com.questnr.model.dto.post.question.PostPollQuestionForCommunityDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.requests.PostPollQuestionRequest;
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
    Page<PostBaseDTO> getAllPostsByCommunityId(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size, @PathVariable long communityId) {
        if (communityPostActionAccessService.hasAccessToPosts(communityId)) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            return communityPostActionService.getAllPostActionsByCommunityId(communityId, pageable);
        }
        throw new AccessException();
    }

    // Get community posts using string of post ids separated by coma
    @RequestMapping(value = "/community/{communityId}/notification/posts", method = RequestMethod.GET)
    Page<PostBaseDTO> getCommunityPostsUsingIdList(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "4") int size,
                                                   @PathVariable long communityId,
                                                   @RequestParam String posts) {
        if (communityPostActionAccessService.hasAccessToPosts(communityId)) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            return communityPostActionService.getCommunityPostsUsingIdList(communityId, posts, null, pageable);
        }
        throw new AccessException();
    }

    // Get community posts using last post id
    @RequestMapping(value = "/community/{communityId}/notification/last/posts", method = RequestMethod.GET)
    Page<PostBaseDTO> getCommunityPostsUsingLastPostId(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "4") int size,
                                                   @PathVariable long communityId,
                                                   @RequestParam Long lastPostId) {
        if (communityPostActionAccessService.hasAccessToPosts(communityId)) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            return communityPostActionService.getCommunityPostsUsingIdList(communityId, null, lastPostId, pageable);
        }
        throw new AccessException();
    }

    @RequestMapping(value = "/community/{communityId}/posts", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    PostActionFeedDTO createPost(@PathVariable long communityId, PostActionRequestDTO postActionRequestDTO) {
        /*
         * Community Post Security Checking
         * */
        if (communityPostActionAccessService.hasAccessToPosts(communityId)) {
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
    PostActionFeedDTO updatePost(@PathVariable long communityId, @PathVariable Long postId, @Valid @RequestBody PostActionUpdateRequestDTO postActionRequest) {
        /*
         * Community Post Security Checking
         * */
        if (communityPostActionAccessService.hasAccessToPostModification(communityId, postId)) {
            return postActionService.updatePostAction(postId, communityId, postActionRequest);
        } else {
            throw new AccessException();
        }
    }

    @RequestMapping(value = "/community/{communityId}/posts/poll/question", method = RequestMethod.POST)
    PostPollQuestionForCommunityDTO createPollQuestionPost(@PathVariable long communityId, @Valid @RequestBody PostPollQuestionRequest postPollQuestionRequest) {
        /*
         * Community Post Security Checking
         * */
        if (communityPostActionAccessService.hasAccessToPosts(communityId)) {
            return communityPostActionService.createPollQuestionPost(communityId, postPollQuestionRequest);
        }
        throw new AccessException();
    }

//    @RequestMapping(value = "/community/{communityId}/posts/{postId}/poll/answer", method = RequestMethod.POST)
//    PollQuestionDTO createPollAnswerPost(@PathVariable Long communityId,
//                                         @PathVariable Long postId,
//                                         @Valid @RequestBody PostPollAnswerRequest postPollAnswerRequest) {
//        if (communityPostActionAccessService.hasAccessToAnswerOnPollQuestion(communityId, postId)) {
//            return communityPostActionService.createPollAnswerPost(postId, postPollAnswerRequest);
//        }
//        throw new AccessException();
//    }

    @RequestMapping(value = "/community/{communityId}/posts/{postId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void deletePost(@PathVariable long communityId, @PathVariable Long postId) {
        /*
         * Community Post Security Checking
         * */
        if (communityPostActionAccessService.hasAccessToPostDeletion(communityId, postId)) {
            postActionService.deletePostAction(postId, communityId);
        } else {
            throw new AccessException();
        }
    }

    // Get the list of poll questions of Community
    @RequestMapping(value = "/community/{communityId}/posts/poll/question", method = RequestMethod.GET)
    Page<PostPollQuestionForCommunityDTO> getAllPostPollQuestion(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size, @PathVariable Long communityId) {
        if (communityPostActionAccessService.hasAccessToPosts(communityId)) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<PostAction> postActionPage = communityPostActionService.getAllPostPollQuestion(communityId, pageable);
            List<PostPollQuestionForCommunityDTO> postActionForCommunityDTOS = postActionPage.getContent().stream()
                    .map(postActionMapper::toPostPollQuestionForCommunityDTO).collect(Collectors.toList());
            return new PageImpl<>(postActionForCommunityDTOS, pageable, postActionPage.getTotalElements());
        }
        throw new AccessException();
    }
}
