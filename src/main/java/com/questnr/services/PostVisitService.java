package com.questnr.services;

import com.questnr.exceptions.InvalidRequestException;
import com.questnr.exceptions.ResourceNotFoundException;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.PostVisit;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.PostVisitRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.requests.PostVisitRequest;
import com.questnr.services.user.UserCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostVisitService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostVisitRepository postVisitRepository;

    @Autowired
    PostActionRepository postActionRepository;

    public Page<PostVisit> getAllPostVisitByPostId(Long postId,
                                                   Pageable pageable) {
        PostAction postAction = postActionRepository.findByPostActionId(postId);
        if (postAction != null)
            return postVisitRepository.findByPostAction(postAction, pageable);
        throw new ResourceNotFoundException("Post not found!");
    }

    public void createPostVisit(PostVisitRequest postVisitRequest) {
        if (!CommonService.isNull(postVisitRequest.getPosts())) {
            String[] postIdStringList = postVisitRequest.getPosts().split(",");
            for(String postIdString: postIdStringList){
                try{
                    Long postId = Long.valueOf(postIdString);
                    this.createPostVisit(postId);
                }catch (Exception e){

                }
            }
        }
    }

    public void createPostVisit(Long postId) {
        PostAction postAction = postActionRepository.findByPostActionId(postId);
        User user = userCommonService.getUser();
        if (postId != null) {
            if (!postVisitRepository.existsByPostActionAndUserActor(postAction, user)) {
                try {
//                    PostView postView = postViewService.createPostViewFromPostVisit(user, postAction);
//                    postVisit.setPostView(postView);
                    PostVisit postVisit = new PostVisit();
                    postVisit.addMetadata();
                    postVisit.setUserActor(user);
                    postVisit.setPostAction(postAction);
                    postVisitRepository.save(postVisit);

//                    postVisit.setPostAction(postAction);
//                    postAction.getPostVisitSet().add(postVisit);
//                    postActionRepository.save(postAction);
                } catch (Exception e) {
                    LOGGER.error(PostVisit.class.getName() + " Exception Occurred");
                }
            }
        }
        throw new InvalidRequestException("Error occurred. Please, try again!");
    }
}
