package com.questnr.controllers.user;

import com.questnr.access.user.UserPostActionAccessService;
import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.post.PostBaseDTO;
import com.questnr.model.dto.post.normal.PostActionFeedDTO;
import com.questnr.model.dto.post.normal.PostActionRequestDTO;
import com.questnr.model.dto.post.normal.PostActionUpdateRequestDTO;
import com.questnr.model.dto.post.question.PollQuestionDTO;
import com.questnr.model.dto.post.question.PostPollQuestionFeedDTO;
import com.questnr.model.dto.post.question.PostPollQuestionForCommunityDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.requests.PostPollAnswerRequest;
import com.questnr.requests.PostPollQuestionRequest;
import com.questnr.services.PostActionService;
import com.questnr.services.user.UserPostActionService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserPostActionController {

    @Autowired
    UserPostActionAccessService userPostActionAccessService;

    @Autowired
    UserPostActionService userPostActionService;

    @Autowired
    PostActionService postActionService;

    @Autowired
    final PostActionMapper postActionMapper;

    UserPostActionController() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    // Basic post operations for users.
    @RequestMapping(value = "/{userId}/posts", method = RequestMethod.GET)
    Page<PostBaseDTO> getAllPostsByUserId(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size, @PathVariable Long userId) {
        User user = userPostActionAccessService.getAllPostsByUserId(userId);
        if (user != null) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            return userPostActionService.getAllPostsByUserId(user, pageable);
        }
        throw new AccessException();
    }

    @RequestMapping(value = "/posts", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    PostActionFeedDTO createPost(PostActionRequestDTO postActionRequestDTO) {
        if (postActionRequestDTO.getFiles() != null && postActionRequestDTO.getFiles().size() > 0) {
            return userPostActionService.creatPostAction(postActionMapper.fromPostActionRequestDTO(postActionRequestDTO), postActionRequestDTO.getFiles());
        } else {
            return userPostActionService.creatPostAction(postActionMapper.fromPostActionRequestDTO(postActionRequestDTO));
        }
    }

    @RequestMapping(value = "/posts/{postId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    void updatePost(@PathVariable Long postId, @Valid @RequestBody PostActionUpdateRequestDTO postActionRequest) {
        if (userPostActionAccessService.hasAccessToPostModification(postId)) {
            postActionService.updatePostAction(postId, postActionRequest);
        } else {
            throw new AccessException();
        }
    }

    @RequestMapping(value = "/posts/poll/question", method = RequestMethod.POST)
    PostPollQuestionForCommunityDTO createPollQuestionPost(@Valid @RequestBody PostPollQuestionRequest postPollQuestionRequest) {
        return userPostActionService.createPollQuestionPost(postPollQuestionRequest);
    }

    @RequestMapping(value = "/posts/{postId}/poll/answer", method = RequestMethod.POST)
    PollQuestionDTO createPollAnswerPost(@PathVariable Long postId, @Valid @RequestBody PostPollAnswerRequest postPollAnswerRequest) {
        PostAction postAction = userPostActionAccessService.createPollAnswerPost(postId);
        if (postAction != null) {
            return userPostActionService.createPollAnswerPost(postAction, postPollAnswerRequest);
        }
        throw new AccessException();
    }

//    @RequestMapping(value = "/posts/question/poll/{postId}", method = RequestMethod.PUT)
//    @ResponseStatus(HttpStatus.OK)
//    void updatePollQuestionPost(@PathVariable Long postId, @Valid @RequestBody PollQuestionPostRequest pollQuestionPostRequest) {
//        PostAction postAction = userPostActionAccessService.hasAccessToPostModification(postId);
//        postActionService.updatePollQuestionPost(postAction, pollQuestionPostRequest);
//    }

    @RequestMapping(value = "/posts/{postId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void deletePost(@PathVariable Long postId) {
        if (userPostActionAccessService.hasAccessToPostDeletion(postId)) {
            postActionService.deletePostAction(postId);
        } else {
            throw new AccessException();
        }
    }

    // Get the list of poll questions for User Profile
    @RequestMapping(value = "/{userId}/posts/poll/question", method = RequestMethod.GET)
    Page<PostPollQuestionFeedDTO> getAllPostPollQuestion(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size, @PathVariable Long userId) {
        User user = userPostActionAccessService.getAllPostsByUserId(userId);
        if (user != null) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            return userPostActionService.getAllPostPollQuestion(user, pageable);
        }
        throw new AccessException();
    }

}
