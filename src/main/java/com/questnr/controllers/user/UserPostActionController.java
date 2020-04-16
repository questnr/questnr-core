package com.questnr.controllers.user;

import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.dto.PostActionRequestDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.services.user.UserPostActionService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserPostActionController {
    @Autowired
    UserPostActionService userPostActionService;

    @Autowired
    final PostActionMapper postActionMapper;

    UserPostActionController() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    // Basic post operations for users.
    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    Page<PostActionDTO> getAllPostsByUserId(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PostAction> postActionPage = userPostActionService.getAllPostActionsByUserId(pageable);
        List<PostActionDTO> postActionDTOS = postActionPage.getContent().stream()
                .map(postActionMapper::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(postActionDTOS, pageable, postActionPage.getTotalElements());
    }

    @RequestMapping(value = "/posts", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    PostActionDTO createPost(PostActionRequestDTO postActionRequestDTO) {
        if(postActionRequestDTO.getFiles() != null && postActionRequestDTO.getFiles().size() > 0) {
            return postActionMapper.toDTO(userPostActionService.creatPostAction(postActionMapper.fromPostActionRequestDTO(postActionRequestDTO), postActionRequestDTO.getFiles()));
        }else{
            return postActionMapper.toDTO(userPostActionService.creatPostAction(postActionMapper.fromPostActionRequestDTO(postActionRequestDTO)));
        }
    }

    @RequestMapping(value = "/posts/{postId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    void updatePost(@PathVariable Long postId, @Valid @RequestBody PostAction postActionRequest) {
        userPostActionService.updatePostAction(postId, postActionRequest);
    }

    @RequestMapping(value = "/posts/{postId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    void deletePost(@PathVariable Long postId) {
        userPostActionService.deletePostAction(postId);
    }
}
