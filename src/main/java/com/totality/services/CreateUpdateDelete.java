package com.totality.services;

import com.totality.exceptions.InvalidInputException;
import com.totality.model.entities.Community;
import com.totality.model.entities.Post;
import com.totality.model.repositories.CommunityRepository;
import com.totality.model.repositories.PostRepository;
import com.totality.requests.CommunityRequests;
import com.totality.responses.CommunityResponse;
import com.totality.security.JwtTokenUtil;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.apache.tomcat.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUpdateDelete {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  @Autowired
  JwtTokenUtil jwtTokenUtil;

  @Autowired
  CommunityRepository communityRepository;

  @Autowired
  PostRepository postRepository;

  public Boolean createCommunity (Community requests){
    long userId = jwtTokenUtil.getLoggedInUserID();
    Boolean response = false;
    if (requests!= null) {
      try {
        requests.setOwnerId(userId);
        requests.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        requests.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
       communityRepository.saveAndFlush(requests);
       response = true;
      } catch (Exception e) {
        LOGGER.error(Community.class.getName() + " Exception Occured");
        response =false;
      }
    } else {
      throw new InvalidInputException(Community.class.getName(), null, null);
    }
    return response;
  }

  public Boolean creatPost(Post post){
    long userId = jwtTokenUtil.getLoggedInUserID();
    Boolean response = false;
    if(post != null){
      try{
        post.setCreatedBy((int) userId);
        post.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        post.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        post.setPostDate(Timestamp.valueOf(LocalDateTime.now()));
        postRepository.saveAndFlush(post);
        response = true;
      }catch (Exception e){
        LOGGER.error(Post.class.getName() + " Exception Occured");
        response =false;
      }
    }else {
      throw new InvalidInputException(Post.class.getName(), null, null);
    }
    return response;
  }


}
