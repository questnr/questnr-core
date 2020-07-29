package com.questnr.services.community;

import com.questnr.model.dto.community.CommunityDTO;
import com.questnr.model.dto.community.CommunityMetaTagCardDTO;
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

    private List<CommunityMetaInformation> getCommunityMetaInformationList(CommunityDTO communityDTO) {
        List<CommunityMetaInformation> communityMetaInformationList = new ArrayList<>();

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "name",
                "description",
                CommonService.removeSpecialCharacters(communityDTO.getDescription())
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "name",
                "author",
                communityDTO.getOwnerUserDTO().getFirstName() + " " + communityDTO.getOwnerUserDTO().getFirstName()
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
                sharableLinkService.getCommunitySharableLink(communityDTO.getSlug()).getClickAction()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "og:title",
                communityDTO.getCommunityName()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "og:image",
                communityDTO.getAvatarDTO().getAvatarLink().replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)","")
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
                "fb:app_id",
                fbAppId
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "twitter:title",
                communityDTO.getCommunityName()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "twitter:description",
                communityDTO.getDescription()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "twitter:url",
                sharableLinkService.getCommunitySharableLink(communityDTO.getSlug()).getClickAction()
        ));

        communityMetaInformationList.add(this.getCommunityMetaInformation(
                "property",
                "twitter:image",
                communityDTO.getAvatarDTO().getAvatarLink().replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)","")
        ));

        return communityMetaInformationList;
    }
    public CommunityDTO setCommunityMetaInformation(CommunityDTO communityDTO) {
        if (communityDTO != null) {
            communityDTO.getMetaList().addAll(this.getCommunityMetaInformationList(communityDTO));
        }
        return communityDTO;
    }

    public CommunityMetaTagCardDTO getCommunityMetaCard(CommunityDTO communityDTO){
        CommunityMetaTagCardDTO communityMetaCard = new CommunityMetaTagCardDTO();
        communityMetaCard.setTitle(communityDTO.getCommunityName());
        communityMetaCard.getMetaList().addAll(this.getCommunityMetaInformationList(communityDTO));
        return communityMetaCard;
    }

    public CommunityMetaTagCardDTO getCommunityMetaCard(Community community){
        return this.getCommunityMetaCard(communityMapper.toDTO(community));
    }

    public CommunityMetaTagCardDTO getCommunityMetaCard(String communitySlug){
        return this.getCommunityMetaCard(communityMapper.toDTO(
                communityCommonService.getCommunity(communitySlug)));
    }
}
