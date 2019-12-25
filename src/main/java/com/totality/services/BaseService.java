package com.totality.services;

import com.totality.model.entities.LikeAction;
import com.totality.model.repositories.CommunityRepository;
import com.totality.model.repositories.LikeActionRepository;
import com.totality.model.repositories.PostRepository;
import com.totality.responses.CommunityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseService {

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    LikeActionRepository likeActionRepository;

    @Autowired
    PostRepository postRepository;

    public boolean slugExistence(String slug, String type) {

        boolean flag = false;
        if (type.equalsIgnoreCase("community")) {
            CommunityResponse community = communityRepository.findAllByCommunityName(slug);
            if (community != null) {
                flag = true;
            }
        } else if (type.equalsIgnoreCase("post")) {
            CommunityResponse community = communityRepository.findAllByCommunityName(slug);
            if (community != null) {
                flag = true;
            }
        }
//    else if (type.equalsIgnoreCase("institute")) {
//      CommunityResponse community = communityRepository.findAllByCommunityName(slug);
//      if (community != null) {
//        flag = true;
//      }
//    }

        return flag;

    }

//    public LikeResponse createPostLike(){
//
//    }

    public Long getPostLikeCount(Long postId) {
        Long likeCount = likeActionRepository.countByPost(postRepository.findByPostId(postId));
        return likeCount;
    }

    public List<LikeAction> getPostLikeList(Long postId) {
        return likeActionRepository.findByPost(postRepository.findByPostId(postId));
    }

}
