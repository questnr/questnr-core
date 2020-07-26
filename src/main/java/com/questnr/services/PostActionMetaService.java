package com.questnr.services;

import com.questnr.model.dto.post.normal.PostActionPublicDTO;
import com.questnr.model.dto.post.question.PostPollQuestionPublicDTO;
import com.questnr.model.entities.MetaInformation;
import com.questnr.model.entities.PostActionMetaInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostActionMetaService {
    @Autowired
    private SharableLinkService sharableLinkService;

    @Autowired
    PostActionService postActionService;

    @Value("${app.name}")
    private String appName;

    @Value("${facebook.appid}")
    private String fbAppId;

    final int MAX_POST_TEXT_LENGTH = 25;

    private PostActionMetaInformation getPostActionMetaInformation(String attrType, String type, String content) {
        MetaInformation metaInfo = new MetaInformation();
        metaInfo.setAttributeType(attrType);
        metaInfo.setType(type);
        metaInfo.setContent(content);
        PostActionMetaInformation postActionMetaInformation = new PostActionMetaInformation();
        postActionMetaInformation.setMetaInformation(metaInfo);
        return postActionMetaInformation;
    }

    private List<PostActionMetaInformation> getPostActionMetaInformationList(PostActionPublicDTO postActionPublicDTO) {
        List<PostActionMetaInformation> postActionMetaInformationList = new ArrayList<>();

        String postText = "";

        if (postActionPublicDTO.getPostData().getText().length() > 0) {
            postText = postActionService.getPostActionTitleTag(postActionPublicDTO.getPostData().getText());
            postActionMetaInformationList.add(this.getPostActionMetaInformation(
                    "name",
                    "description",
                    postText
            ));
        }

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "name",
                "author",
                postActionPublicDTO.getUserDTO().getFirstName() + " " + postActionPublicDTO.getUserDTO().getFirstName()
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "name",
                "robots",
                "index, follow, max-image-preview:standard"
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "name",
                "googlebot",
                "index, follow, max-image-preview:standard"
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "og:url",
                sharableLinkService.getCommunitySharableLink(postActionPublicDTO.getSlug()).getClickAction()
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "og:title",
                appName
        ));

        if (postActionPublicDTO.getPostMediaList().size() > 0) {
            postActionMetaInformationList.add(this.getPostActionMetaInformation(
                    "property",
                    "og:image",
                    postActionPublicDTO.getPostMediaList().get(0).getPostMediaLink()
            ));
        }
        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "og:type",
                "website"
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "fb:app_id",
                fbAppId
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "twitter:title",
                appName
        ));

        if (postText.length() > 0) {
            postActionMetaInformationList.add(this.getPostActionMetaInformation(
                    "property",
                    "twitter:description",
                    postText
            ));
        }

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "twitter:url",
                sharableLinkService.getCommunitySharableLink(postActionPublicDTO.getSlug()).getClickAction()
        ));

        if (postActionPublicDTO.getPostMediaList().size() > 0) {
            postActionMetaInformationList.add(this.getPostActionMetaInformation(
                    "property",
                    "twitter:image",
                    postActionPublicDTO.getPostMediaList().get(0).getPostMediaLink()
            ));
        }

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "fb:app_id",
                fbAppId
        ));

        return postActionMetaInformationList;
    }

    private List<PostActionMetaInformation> getPostActionMetaInformationList(PostPollQuestionPublicDTO postPollQuestionPublicDTO) {
        List<PostActionMetaInformation> postActionMetaInformationList = new ArrayList<>();

        String postText = "";

        if (postPollQuestionPublicDTO.getPollQuestion().getQuestion().length() > 0) {
            postText = postActionService.getPostActionTitleTag(postPollQuestionPublicDTO.getPollQuestion().getQuestion());
            postActionMetaInformationList.add(this.getPostActionMetaInformation(
                    "name",
                    "description",
                    postText
            ));
        }

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "name",
                "author",
                postPollQuestionPublicDTO.getUserDTO().getFirstName() + " " + postPollQuestionPublicDTO.getUserDTO().getFirstName()
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "name",
                "robots",
                "index, follow, max-image-preview:standard"
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "name",
                "googlebot",
                "index, follow, max-image-preview:standard"
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "og:url",
                sharableLinkService.getCommunitySharableLink(postPollQuestionPublicDTO.getSlug()).getClickAction()
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "og:title",
                appName
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "og:type",
                "website"
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "fb:app_id",
                fbAppId
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "twitter:title",
                appName
        ));

        if (postText.length() > 0) {
            postActionMetaInformationList.add(this.getPostActionMetaInformation(
                    "property",
                    "twitter:description",
                    postText
            ));
        }

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "twitter:url",
                sharableLinkService.getCommunitySharableLink(postPollQuestionPublicDTO.getSlug()).getClickAction()
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "fb:app_id",
                fbAppId
        ));

        return postActionMetaInformationList;
    }

    public PostActionPublicDTO setPostActionMetaInformation(PostActionPublicDTO postActionPublicDTO) {
        if (postActionPublicDTO != null) {
            postActionPublicDTO.getMetaList().addAll(this.getPostActionMetaInformationList(postActionPublicDTO));
        }
        return postActionPublicDTO;
    }

    public PostPollQuestionPublicDTO setPostActionMetaInformation(PostPollQuestionPublicDTO postPollQuestionPublicDTO) {
        if (postPollQuestionPublicDTO != null) {
            postPollQuestionPublicDTO.getMetaList().addAll(this.getPostActionMetaInformationList(postPollQuestionPublicDTO));
        }
        return postPollQuestionPublicDTO;
    }
}
