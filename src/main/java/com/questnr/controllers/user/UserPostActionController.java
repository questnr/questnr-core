package com.questnr.controllers.user;

import com.questnr.model.dto.PostActionRequestDTO;
import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.services.user.UserPostActionService;
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
public class UserPostActionController {
    @Autowired
    UserPostActionService userPostActionService;

    @Autowired
    final PostActionMapper postActionMapper;

    UserPostActionController() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    // Basic post operations for users.
    @RequestMapping(value = "user/posts", method = RequestMethod.GET)
    Page<PostActionDTO> getAllPostsByUserId(Pageable pageable) {
        Page<PostAction> page = userPostActionService.getAllPostActionsByUserId(pageable);
        return new PageImpl<PostActionDTO>(postActionMapper.toDTOs(page.getContent()), pageable, page.getTotalElements());
    }

    @RequestMapping(value = "user/posts", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    PostActionDTO createPost(@Valid PostActionRequestDTO postActionRequestDTO, @RequestParam(value = "file") List<MultipartFile> files) {
        return postActionMapper.toDTO(userPostActionService.creatPostAction(postActionMapper.fromPostActionRequestDTO(postActionRequestDTO), files));
    }

    @RequestMapping(value = "user/posts/{postId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    void updatePost(@PathVariable Long postId, @Valid @RequestBody PostAction postActionRequest) {
        userPostActionService.updatePostAction(postId, postActionRequest);
    }

    @RequestMapping(value = "user/posts/{postId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void deletePost(@PathVariable Long postId) {
        userPostActionService.deletePostAction(postId);
    }
}
