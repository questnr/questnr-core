package com.questnr.base;

import com.questnr.model.dto.PostActionDTO;
import com.questnr.model.entities.PostAction;
import com.questnr.model.mapper.PostActionMapper;
import com.questnr.requests.PostActionRequest;
import com.questnr.services.PostActionService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class PostActionController {

    @Autowired
    PostActionService postActionService;

    @Autowired
    final PostActionMapper postActionMapper;

    PostActionController() {
        postActionMapper = Mappers.getMapper(PostActionMapper.class);
    }

    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    Page<PostActionDTO> getAllPostsByUserId(Pageable pageable) {
        Page<PostAction> page = postActionService.getAllPostActionsByUserId(pageable);
        return new PageImpl<PostActionDTO>(postActionMapper.toDTOs(page.getContent()), pageable, page.getTotalElements());
    }

    @RequestMapping(value = "/posts", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    PostActionDTO createPost(@Valid PostActionRequest postActionRequest, @RequestParam(value = "file") List<MultipartFile> files) {
        return postActionMapper.toDTO(postActionService.creatPostAction(postActionRequest, files));
    }

    @RequestMapping(value = "/posts/{postId}", method = RequestMethod.PUT)
    public PostAction updatePost(@PathVariable Long postId, @Valid @RequestBody PostAction postActionRequest) {
        return postActionService.updatePostAction(postId, postActionRequest);
    }

    @RequestMapping(value = "/posts/{postId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        return postActionService.deletePostAction(postId);
    }

}
