package com.questnr.services;

import com.questnr.common.enums.PostEditorType;
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

    @Value("${app.logo.link}")
    private String appLogoLink;

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
        String defaultTitle = "Post | by @" + postActionPublicDTO.getUserDTO().getUsername();
        boolean isThisBlog = postActionPublicDTO.getPostData().getPostEditorType() == PostEditorType.blog;

        String postText = defaultTitle;

        if (postActionPublicDTO.getPostData().getText().length() > 0) {
            postText = postActionService.getPostActionTitleTag(postActionPublicDTO.getPostData().getText());
        }

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "name",
                "description",
                postText
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "name",
                "keywords",
                postActionPublicDTO.getTags()
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "name",
                "author",
                postActionPublicDTO.getUserDTO().getDisplayName()
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
                isThisBlog ?
                        postActionPublicDTO.getPostData().getBlogTitle() +
                                " | by @" + postActionPublicDTO.getUserDTO().getUsername() :
                        defaultTitle
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "name",
                "og:description",
                postText
        ));

        if (postActionPublicDTO.getPostMediaList().size() > 0) {
            postActionMetaInformationList.add(this.getPostActionMetaInformation(
                    "property",
                    "og:image",
                    postActionPublicDTO.getPostMediaList().get(0).getPostMediaLink()
            ));
        } else {
            postActionMetaInformationList.add(this.getPostActionMetaInformation(
                    "property",
                    "og:image",
                    appLogoLink
            ));
        }

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "og:type",
                isThisBlog ? "blog" : "website"
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "og:site_name",
                appName
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "twitter:title",
                isThisBlog ?
                        postActionPublicDTO.getPostData().getBlogTitle() +
                                " | by @" + postActionPublicDTO.getUserDTO().getUsername() :
                        defaultTitle
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "twitter:description",
                postText
        ));

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
        } else {
            postActionMetaInformationList.add(this.getPostActionMetaInformation(
                    "property",
                    "twitter:image",
                    appLogoLink
            ));
        }

        if (postActionPublicDTO.getPostMediaList().size() > 0) {
            postActionMetaInformationList.add(this.getPostActionMetaInformation(
                    "property",
                    "twitter:image:src",
                    postActionPublicDTO.getPostMediaList().get(0).getPostMediaLink()
            ));
        } else {
            postActionMetaInformationList.add(this.getPostActionMetaInformation(
                    "property",
                    "twitter:image:src",
                    appLogoLink
            ));
        }

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "twitter:card",
                "summary_large_image"
        ));


        return postActionMetaInformationList;
    }

    private List<PostActionMetaInformation> getPostActionMetaInformationList(PostPollQuestionPublicDTO postPollQuestionPublicDTO) {
        List<PostActionMetaInformation> postActionMetaInformationList = new ArrayList<>();
        String defaultTitle = "Question | by @" + postPollQuestionPublicDTO.getUserDTO().getUsername();

        String postText = defaultTitle;

        if (postPollQuestionPublicDTO.getQuestionText().length() > 0) {
            postText = postActionService.getPostActionTitleTag(postPollQuestionPublicDTO.getQuestionText());
        }

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "name",
                "description",
                postText
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "name",
                "keywords",
                postPollQuestionPublicDTO.getTags()
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "name",
                "author",
                postPollQuestionPublicDTO.getUserDTO().getDisplayName()
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
                defaultTitle
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "og:description",
                postText
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "og:image",
                appLogoLink
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "og:type",
                "website"
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "og:site_name",
                defaultTitle
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "twitter:title",
                defaultTitle
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "twitter:description",
                postText
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "twitter:url",
                sharableLinkService.getCommunitySharableLink(postPollQuestionPublicDTO.getSlug()).getClickAction()
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "twitter:image",
                appLogoLink
        ));


        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "twitter:image:src",
                appLogoLink
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "twitter:card",
                "summary_large_image"
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
