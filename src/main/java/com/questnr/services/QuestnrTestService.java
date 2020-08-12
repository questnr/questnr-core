package com.questnr.services;

import com.questnr.common.UserInterests;
import com.questnr.common.enums.PostEditorType;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.PostAction;
import com.questnr.model.entities.StaticInterest;
import com.questnr.model.entities.User;
import com.questnr.model.repositories.CommunityRepository;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.model.repositories.StaticInterestRepository;
import com.questnr.model.repositories.UserRepository;
import com.questnr.services.user.UserCommonService;
import com.questnr.util.VideoCompression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestnrTestService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    private CommonService commonService;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    PostActionService postActionService;

    @Autowired
    AmazonS3Client amazonS3Client;

    @Autowired
    CommunityRepository communityRepository;

    @Autowired
    UserRepository userRepository;

    @Value("${app.icon-prefix}")
    private String ICON_PREFIX;

    @Value("${app.small-prefix}")
    private String SMALL_PREFIX;

    @Value("${app.medium-prefix}")
    private String MEDIUM_PREFIX;

    @Autowired
    private StaticInterestRepository staticInterestRepository;

    public String manipulateVideo(MultipartFile multipartFile, int quality) throws IOException, InterruptedException {
        File source = commonService.convertMultiPartToFile(multipartFile);
        File target = new File("out_" + source.getName());
        VideoCompression videoCompression = new VideoCompression(source, target);
        videoCompression.setQuality(quality);
        Thread videoCompressionThread = new Thread(videoCompression, "VideoCompression-1");
        videoCompressionThread.start();
        videoCompressionThread.join();
        return "Successful";
    }

    public Map<String, Integer> makeBlogTitle() {
        List<PostAction> postActionList = postActionRepository.findAll();
        Map<String, Integer> response = new HashMap<>();
        int count = 0;
        int blogCount = 0;
        for (PostAction postAction : postActionList) {
            if (postAction.getPostEditorType() == PostEditorType.blog) {
                if (CommonService.isNull(postAction.getBlogTitle())) {
                    postAction.setTags(postActionService.getPostActionTitleTag(postAction.getText()));
                    postAction.setBlogTitle(postAction.getTags().substring(0, Math.min(postAction.getTags().length(), 20))+ "...");
                    postActionRepository.save(postAction);
                    count++;
                }
                blogCount++;
            }
        }
        response.put("affected", count);
        response.put("totalBlogs", blogCount);
        return response;
    }

    public Map<String, String> copyAvatars(){
        Map<String, String> response = new HashMap<>();
        List<Community> communityList = communityRepository.findAll();
        for(Community community: communityList){
            if(!(community.getAvatar() == null || !(CommonService.isNull(community.getAvatar().getFileName()) || CommonService.isNull(community.getAvatar().getAvatarKey())))) {
                    try{
                        String key = "";
                        if(CommonService.isNull(community.getAvatar().getFileName())) {
                            key = community.getAvatar().getAvatarKey();
                            this.amazonS3Client.copyToPublicAssets(key);
//                        this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(community.getAvatar().getAvatarKey());
                        } else {
                            List <String> fileList = new ArrayList<>();
                            String fileName = community.getAvatar().getFileName();
                            String pathToDir = community.getAvatar().getPathToDir();
                            key = Paths.get(pathToDir, fileName).toString();
                            fileList.add(Paths.get(pathToDir, fileName).toString());
                            fileList.add(Paths.get(pathToDir, ICON_PREFIX+fileName).toString());
                            fileList.add(Paths.get(pathToDir, SMALL_PREFIX+fileName).toString());
                            fileList.add(Paths.get(pathToDir, MEDIUM_PREFIX+fileName).toString());
                            this.amazonS3Client.copyToPublicAssets(fileList);
//                        this.amazonS3Client.deleteAvatarFromS3(community.getAvatar());
                        }
                        response.put("Community: " + community.getCommunityId(), key);
                    }catch (Exception e){
                        response.put("Community Avatar Error: " + community.getCommunityId().toString(), e.getMessage());
                    }
            }
        }

        List<User> userList = userRepository.findAll();
        for(User user: userList){
            try{
                if(!(user.getAvatar() == null || !(CommonService.isNull(user.getAvatar().getFileName()) || CommonService.isNull(user.getAvatar().getAvatarKey())))) {
                    String key = "";
                    if(CommonService.isNull(user.getAvatar().getFileName())) {
                        key = user.getAvatar().getAvatarKey();
                        this.amazonS3Client.copyToPublicAssets(key);
//                        this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(community.getAvatar().getAvatarKey());
                    } else {
                        List <String> fileList = new ArrayList<>();
                        String fileName = user.getAvatar().getFileName();
                        String pathToDir = user.getAvatar().getPathToDir();
                        key = Paths.get(pathToDir, fileName).toString();
                        fileList.add(Paths.get(pathToDir, fileName).toString());
                        fileList.add(Paths.get(pathToDir, ICON_PREFIX+fileName).toString());
                        fileList.add(Paths.get(pathToDir, SMALL_PREFIX+fileName).toString());
                        fileList.add(Paths.get(pathToDir, MEDIUM_PREFIX+fileName).toString());
                        this.amazonS3Client.copyToPublicAssets(fileList);
//                        this.amazonS3Client.deleteAvatarFromS3(community.getAvatar());
                    }
                    response.put("User Avatar: " + user.getUserId(), key);
                }
            }catch (Exception e){
                response.put("User Avatar Error: " + user.getUserId().toString(), e.getMessage());
            }

            try{
                if(!(user.getBanner() == null || CommonService.isNull(user.getBanner().getFileName()))) {
                    List <String> fileList = new ArrayList<>();
                    String fileName = user.getBanner().getFileName();
                    String pathToDir = user.getBanner().getPathToDir();
                    String key = Paths.get(pathToDir, fileName).toString();
                    fileList.add(Paths.get(pathToDir, fileName).toString());
                    fileList.add(Paths.get(pathToDir, ICON_PREFIX+fileName).toString());
                    fileList.add(Paths.get(pathToDir, SMALL_PREFIX+fileName).toString());
                    fileList.add(Paths.get(pathToDir, MEDIUM_PREFIX+fileName).toString());
                    this.amazonS3Client.copyToPublicAssets(fileList);
//                        this.amazonS3Client.deleteAvatarFromS3(community.getAvatar());
                    response.put("User Banner: " + user.getUserId(), key);
                }
            }catch (Exception e){
                response.put("User Banner Error: " + user.getUserId().toString(), e.getMessage());
            }
        }
        return response;
    }

    public Map<String, String> setPublicTagToExisting(){
        Map<String, String> response = new HashMap<>();
        List<Community> communityList = communityRepository.findAll();
        for(Community community: communityList){
            if(!(community.getAvatar() == null || !(CommonService.isNull(community.getAvatar().getFileName()) || CommonService.isNull(community.getAvatar().getAvatarKey())))) {
                try{
                    String key = "";
                    if(CommonService.isNull(community.getAvatar().getFileName())) {
                        key = community.getAvatar().getAvatarKey();
                        this.amazonS3Client.makeObjectPublic(key);
//                        this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(community.getAvatar().getAvatarKey());
                    } else {
                        List <String> fileList = new ArrayList<>();
                        String fileName = community.getAvatar().getFileName();
                        String pathToDir = community.getAvatar().getPathToDir();
                        key = Paths.get(pathToDir, fileName).toString();
                        fileList.add(Paths.get(pathToDir, fileName).toString());
                        fileList.add(Paths.get(pathToDir, ICON_PREFIX+fileName).toString());
                        fileList.add(Paths.get(pathToDir, SMALL_PREFIX+fileName).toString());
                        fileList.add(Paths.get(pathToDir, MEDIUM_PREFIX+fileName).toString());
                        this.amazonS3Client.makeObjectPublic(fileList);
//                        this.amazonS3Client.deleteAvatarFromS3(community.getAvatar());
                    }
                    response.put("Community: " + community.getCommunityId(), key);
                }catch (Exception e){
                    response.put("Community Avatar Error: " + community.getCommunityId().toString(), e.getMessage());
                }
            }
        }

        List<User> userList = userRepository.findAll();
        for(User user: userList){
            try{
                if(!(user.getAvatar() == null || !(CommonService.isNull(user.getAvatar().getFileName()) || CommonService.isNull(user.getAvatar().getAvatarKey())))) {
                    String key = "";
                    if(CommonService.isNull(user.getAvatar().getFileName())) {
                        key = user.getAvatar().getAvatarKey();
                        this.amazonS3Client.makeObjectPublic(key);
//                        this.amazonS3Client.deleteFileFromS3BucketUsingPathToFile(community.getAvatar().getAvatarKey());
                    } else {
                        List <String> fileList = new ArrayList<>();
                        String fileName = user.getAvatar().getFileName();
                        String pathToDir = user.getAvatar().getPathToDir();
                        key = Paths.get(pathToDir, fileName).toString();
                        fileList.add(Paths.get(pathToDir, fileName).toString());
                        fileList.add(Paths.get(pathToDir, ICON_PREFIX+fileName).toString());
                        fileList.add(Paths.get(pathToDir, SMALL_PREFIX+fileName).toString());
                        fileList.add(Paths.get(pathToDir, MEDIUM_PREFIX+fileName).toString());
                        this.amazonS3Client.makeObjectPublic(fileList);
//                        this.amazonS3Client.deleteAvatarFromS3(community.getAvatar());
                    }
                    response.put("User Avatar: " + user.getUserId(), key);
                }
            }catch (Exception e){
                response.put("User Avatar Error: " + user.getUserId().toString(), e.getMessage());
            }

            try{
                if(!(user.getBanner() == null || CommonService.isNull(user.getBanner().getFileName()))) {
                    List <String> fileList = new ArrayList<>();
                    String fileName = user.getBanner().getFileName();
                    String pathToDir = user.getBanner().getPathToDir();
                    String key = Paths.get(pathToDir, fileName).toString();
                    fileList.add(Paths.get(pathToDir, fileName).toString());
                    fileList.add(Paths.get(pathToDir, ICON_PREFIX+fileName).toString());
                    fileList.add(Paths.get(pathToDir, SMALL_PREFIX+fileName).toString());
                    fileList.add(Paths.get(pathToDir, MEDIUM_PREFIX+fileName).toString());
                    this.amazonS3Client.makeObjectPublic(fileList);
//                        this.amazonS3Client.deleteAvatarFromS3(community.getAvatar());
                    response.put("User Banner: " + user.getUserId(), key);
                }
            }catch (Exception e){
                response.put("User Banner Error: " + user.getUserId().toString(), e.getMessage());
            }
        }
        return response;
    }

    public void storeUserInterest(String interest){
        try{
            staticInterestRepository.save(new StaticInterest(interest));
        }catch (Exception e){
            LOGGER.error("storeUserInterest: Error while storing interest - "+interest);
        }
    }

    public void storeAllUserInterest(){
        List<String> interestList = UserInterests.getUserInterests();
        for(String interest: interestList) {
            this.storeUserInterest(interest);
        }
    }
}
