package com.questnr.services;

import com.questnr.model.dto.PostActionPublicDTO;
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

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "name",
                "description",
                CommonService.removeSpecialCharacters(postActionPublicDTO.getText().substring(0, Math.max(postActionPublicDTO.getText().length() , MAX_POST_TEXT_LENGTH)))
        ));

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

        if( postActionPublicDTO.getPostMediaList().size() > 0){
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

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "twitter:description",
                CommonService.removeSpecialCharacters(postActionPublicDTO.getText().substring(0, Math.max(postActionPublicDTO.getText().length() , MAX_POST_TEXT_LENGTH)))
        ));

        postActionMetaInformationList.add(this.getPostActionMetaInformation(
                "property",
                "twitter:url",
                sharableLinkService.getCommunitySharableLink(postActionPublicDTO.getSlug()).getClickAction()
        ));

        if( postActionPublicDTO.getPostMediaList().size() > 0) {
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
    public PostActionPublicDTO setPostActionMetaInformation(PostActionPublicDTO postActionPublicDTO) {
        if (postActionPublicDTO != null) {
            postActionPublicDTO.getMetaList().addAll(this.getPostActionMetaInformationList(postActionPublicDTO));
        }
        return postActionPublicDTO;
    }
}
