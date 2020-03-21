package com.questnr.web;

import com.questnr.model.entities.Community;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.services.community.CommunityService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommunityService crudService;


    @RequestMapping(value = "/feeds", method = RequestMethod.GET)
    String index() {

        return "angularProject";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String loginpage() {
        return "angularProject";

//        return "homepage/landingPage";
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    String dashboard() {

        return "admin/index";
    }

    @RequestMapping(value = "/admin/communities", method = RequestMethod.GET)
    String allCommunities(Model model) {
        List<Community> community = communityRepository.findAll();
        model.addAttribute("all", community);
        return "admin/community/index";
    }

    @RequestMapping(value = "/admin/communities/add", method = RequestMethod.GET)
    String addCommunities(Model model) {
        return "admin/community/addCommunity";
    }

    @RequestMapping(value = "/admin/communities/{slug}/update", method = RequestMethod.GET)
    String updateCommunity(@PathVariable("slug") String slug, Model model) {
        Community community = communityRepository.findAllBySlug(slug);
        model.addAttribute("community", community);
        return "admin/community/addCommunity";
    }

    @RequestMapping(value = "/admin/posts/add", method = RequestMethod.GET)
    String addPosts(Model model) {
        List<Community> communities = crudService.findAllCommunityNames();
        model.addAttribute("communities", communities);
        return "admin/post/addPost";
    }

    @RequestMapping(value = "/admin/posts", method = RequestMethod.GET)
    String allPosts(Model model) {
        List<PostAction> posts = postActionRepository.findAll();
        model.addAttribute("all", posts);
        return "admin/post/index";
    }

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    String allUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("all", users);
        return "admin/users/index";
    }

}
