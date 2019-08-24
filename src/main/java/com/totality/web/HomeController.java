package com.totality.web;

import com.totality.model.entities.Community;
import com.totality.model.entities.Post;
import com.totality.model.repositories.CommunityRepository;
import com.totality.model.repositories.PostRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

  @Autowired
  CommunityRepository communityRepository;

  @Autowired
  PostRepository postRepository;


  @RequestMapping(value = "/feeds", method = RequestMethod.GET)
  String index() {

    return "index";
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  String loginpage() {

    return "homepage/loginpage";
  }

  @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
  String dashboard(){

    return "admin/index";
  }

  @RequestMapping(value = "/admin/communities", method = RequestMethod.GET)
  String allCommunities(Model model){
    List<Community> community = communityRepository.findAll();
    model.addAttribute("all", community);
    return "admin/community/index";
  }

  @RequestMapping(value = "/admin/posts", method = RequestMethod.GET)
  String allPosts(Model model){
    List<Post> posts = postRepository.findAll();
    model.addAttribute("all", posts);
    return "admin/post/index";
  }

  @RequestMapping(value = "/admin/communities/add", method = RequestMethod.GET)
  String addCommunities(){
    return "admin/community/addCommunity";
  }

  @RequestMapping(value = "/admin/posts/add", method = RequestMethod.GET)
  String addPosts(){
    return "admin/post/addPost";
  }
}
