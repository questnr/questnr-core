package com.questnr.services;

import com.questnr.common.enums.PostEditorType;
import com.questnr.model.entities.PostAction;
import com.questnr.model.repositories.PostActionRepository;
import com.questnr.services.user.UserCommonService;
import com.questnr.util.VideoCompression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestnrTestService {

    @Autowired
    UserCommonService userCommonService;

    @Autowired
    private CommonService commonService;

    @Autowired
    PostActionRepository postActionRepository;

    @Autowired
    PostActionService postActionService;

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
}
