package com.questnr.services.community;

import com.questnr.model.dto.community.CommunityMetaTagCardDTO;
import com.questnr.model.dto.community.CommunityPublicDTO;
import com.questnr.model.entities.Community;
import com.questnr.model.entities.CommunityMetaInformation;
import com.questnr.model.entities.MetaInformation;
import com.questnr.model.mapper.CommunityMapper;
import com.questnr.services.CommonService;
import com.questnr.services.SharableLinkService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommunityMetaService {
    @Autowired
    private SharableLinkService sharableLinkService;

    @Value("${app.name}")
    private String appName;

    @Value("${facebook.appid}")
    private String fbAppId;

    @Value("${app.logo.link}")
    private String appLogoLink;

    @Autowired
    CommunityMapper communityMapper;

    @Autowired
    CommunityCommonService communityCommonService;

    public CommunityMetaService(){
        communityMapper = Mappers.getMapper(CommunityMapper.class);
    }

    private CommunityMetaInformation getCommunityMetaInformation(String attrType, String type, String content) {
        MetaInformation metaInfo = new MetaInformation();
        metaInfo.setAttributeType(attrType);
        metaInfo.setType(type);
        metaInfo.setContent(content);
        CommunityMetaInformation communityMetaInformation = new CommunityMetaInformation();
        communityMetaInformation.setMetaInformation(metaInfo);
        return communityMetaInformation;
    }

    private List<CommunityMetaInformation> getCommunityMetaInformationList(CommunityPublicDTO communityPublicDTO) {
        List<CommunityMetaInformation> communityMetaInformationList = new ArrayList<>();
        String communityAvatarLink = communityPublicDTO.getAvatarDTO().getAvatarLink() != null ?
                communityPublicDTO.getAvatarDTO().getAvatarLink() :
                appLogoLink;

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "name",
                "description",
                CommonService.removeSpecialCharacters(communityPublicDTO.getDescription())
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "name",
                "author",
                communityPublicDTO.getOwnerUserDTO().getDisplayName()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "name",
                "robots",
                "index, follow, max-image-preview:standard"
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "name",
                "googlebot",
                "index, follow, max-image-preview:standard"
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "og:url",
                sharableLinkService.getCommunitySharableLink(communityPublicDTO.getSlug()).getClickAction()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "og:title",
                communityPublicDTO.getCommunityName()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "og:image",
                communityAvatarLink
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "og:type",
                "website"
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "og:site_name",
                appName
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "twitter:title",
                communityPublicDTO.getCommunityName()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "twitter:description",
                communityPublicDTO.getDescription()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "twitter:url",
                sharableLinkService.getCommunitySharableLink(communityPublicDTO.getSlug()).getClickAction()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "twitter:image",
                communityAvatarLink
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "twitter:image:src",
                communityAvatarLink
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "twitter:card",
                "summary_large_image"
        ));

        return communityMetaInformationList;
    }
    public CommunityPublicDTO setCommunityMetaInformation(CommunityPublicDTO communityPublicDTO) {
        if (communityPublicDTO != null) {
            CommunityMetaTagCardDTO communityMetaTagCardDTO = new CommunityMetaTagCardDTO();
            communityMetaTagCardDTO.setTitle(communityPublicDTO.getCommunityName());
            communityMetaTagCardDTO
                    .getMetaList().addAll(this.getCommunityMetaInformationList(communityPublicDTO));
            communityPublicDTO.setMetaTagCard(communityMetaTagCardDTO);
        }
        return communityPublicDTO;
    }

    public CommunityMetaTagCardDTO getCommunityMetaCard(CommunityPublicDTO communityPublicDTO){
        CommunityMetaTagCardDTO communityMetaCard = new CommunityMetaTagCardDTO();
        communityMetaCard.setTitle(communityPublicDTO.getCommunityName());
        communityMetaCard.getMetaList().addAll(this.getCommunityMetaInformationList(communityPublicDTO));
        return communityMetaCard;
    }

    public CommunityMetaTagCardDTO getCommunityMetaCard(Community community){
        return this.getCommunityMetaCard(communityMapper.toPublicDTO(community));
    }

    public CommunityMetaTagCardDTO getCommunityMetaCard(String communitySlug){
        return this.getCommunityMetaCard(communityMapper.toPublicDTO(
                communityCommonService.getCommunity(communitySlug)));
    }
}
