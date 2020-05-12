package com.questnr.controllers.user;

import com.questnr.access.UserPostActionAccessService;
import com.questnr.exceptions.AccessException;
import com.questnr.model.dto.PostActionCardDTO;
import com.questnr.model.dto.PostActionRequestDTO;
import com.questnr.model.dto.PostActionUpdateRequestDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.mapper.PostActionMapper;
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
    Page<PostActionCardDTO> getAllPostsByUserId(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size, @PathVariable Long userId) {
        User user = userPostActionAccessService.getAllPostsByUserId(userId);
        if (user != null) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            return userPostActionService.getAllPostsByUserId(user, pageable);
        }
        throw new AccessException();
    }

    @RequestMapping(value = "/posts", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    PostActionCardDTO createPost(PostActionRequestDTO postActionRequestDTO) {
        if (postActionRequestDTO.getFiles() != null && postActionRequestDTO.getFiles().size() > 0) {
            return userPostActionService.creatPostAction(postActionMapper.fromPostActionRequestDTO(postActionRequestDTO), postActionRequestDTO.getFiles());
        } else {
            return userPostActionService.creatPostAction(postActionMapper.fromPostActionRequestDTO(postActionRequestDTO));
        }
    }

    @RequestMapping(value = "/posts/{postId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    void updatePost(@PathVariable Long postId, @Valid @RequestBody PostActionUpdateRequestDTO postActionRequest) {
        PostAction postAction = userPostActionAccessService.hasAccessToPostModification(postId);
        postActionService.updatePostAction(postAction, postActionRequest);
    }

    @RequestMapping(value = "/posts/{postId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void deletePost(@PathVariable Long postId) {
        PostAction postAction = userPostActionAccessService.hasAccessToPostDeletion(postId);
        postActionService.deletePostAction(postAction);
    }
}
