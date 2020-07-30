package com.questnr.model.dto.post.question;

import com.questnr.model.dto.community.CommunityForPostActionDTO;
import com.questnr.model.dto.post.normal.PostActionMetaTagCardDTO;
import com.questnr.model.dto.user.UserOtherDTO;

public class PostPollQuestionPublicDTO extends PostPollQuestionDTO {

    private UserOtherDTO userDTO;
    private CommunityForPostActionDTO communityDTO;
    private PostActionMetaTagCardDTO metaTagCard;

    public UserOtherDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserOtherDTO userDTO) {
        this.userDTO = userDTO;
    }

    public CommunityForPostActionDTO getCommunityDTO() {
        return communityDTO;
    }

    public void setCommunityDTO(CommunityForPostActionDTO communityDTO) {
        this.communityDTO = communityDTO;
    }

    public PostActionMetaTagCardDTO getMetaTagCard() {
        return metaTagCard;
    }

    public void setMetaTagCard(PostActionMetaTagCardDTO metaTagCard) {
        this.metaTagCard = metaTagCard;
    }
}
